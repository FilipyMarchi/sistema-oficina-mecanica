const url = "http://localhost:8080/oficina/revisao";

const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
};

// Valores do enum StatusRevisao do servidor
// (o primeiro valor é o status padrão ao agendar uma revisão)
const STATUS_REVISAO = ["EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"];

// Revisões que estão aparecendo na tela (o PUT precisa dos dados completos da revisão)
let revisoesCarregadas = [];

/* ---------- Utilitários ---------- */

function escapar(valor) {
    return String(valor ?? "").replace(/[&<>"']/g, function (c) {
        return { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c];
    });
}

async function lerErro(res, padrao) {
    try {
        const erro = await res.json();
        return erro.erro || padrao;
    } catch (e) {
        return padrao; // corpo não era JSON
    }
}

// EM_ANDAMENTO -> Em andamento
function formatarStatus(status) {
    if (!status) return status;
    const t = String(status).replace(/_/g, " ").toLowerCase();
    return t.charAt(0).toUpperCase() + t.slice(1);
}

function opcoesStatus(selecionado) {
    const lista = STATUS_REVISAO.slice();
    if (selecionado && lista.indexOf(selecionado) === -1) lista.unshift(selecionado);

    return lista.map(function (s) {
        return '<option value="' + escapar(s) + '"' + (s === selecionado ? " selected" : "") + '>' +
               escapar(formatarStatus(s)) + '</option>';
    }).join("");
}

// Preenche o <select id="status"> da página de agendar
function preencherStatus() {
    const select = document.getElementById("status");
    if (select) select.innerHTML = opcoesStatus(STATUS_REVISAO[0]);
}

// A placa e o mecânico podem vir direto na revisão ou dentro de objetos aninhados
function placaDaRevisao(r) {
    return r.veiculoPlaca ?? (r.veiculo && r.veiculo.placa) ?? "";
}

function nomeDoMecanico(r) {
    return r.mecanicoNome ?? (r.mecanico && r.mecanico.nome) ?? "";
}

// Aceita "2026-11-10" ou [2026, 11, 10] e devolve "2026-11-10"
function dataIso(d) {
    if (Array.isArray(d) && d.length >= 3) {
        return d[0] + "-" + String(d[1]).padStart(2, "0") + "-" + String(d[2]).padStart(2, "0");
    }
    if (typeof d === "string") return d.slice(0, 10);
    return "";
}

// 2026-11-10 -> 10/11/2026
function formatarData(d) {
    const iso = dataIso(d);
    if (!iso) return d;
    const p = iso.split("-");
    return p[2] + "/" + p[1] + "/" + p[0];
}

function formatarKm(valor) {
    const n = Number(valor);
    if (valor === null || valor === undefined || valor === "" || isNaN(n)) return valor;
    return n.toLocaleString("pt-BR") + " km";
}

// Busca revisão(ões) pela placa do veículo (GET /placa/{placa})
async function consultarPorPlaca(placa) {
    const res = await fetch(url + "/placa/" + encodeURIComponent(placa), {
        method: "GET",
        headers: { "Accept": "application/json" }
    });
    if (res.status === 404) throw new Error("Revisão não encontrada.");
    if (!res.ok) throw new Error(await lerErro(res, "Servidor respondeu " + res.status));

    const texto = await res.text();
    if (!texto) throw new Error("Revisão não encontrada."); // servidor respondeu sem corpo
    return JSON.parse(texto);
}

/* ---------- Agendar ---------- */

function lerCampos() {
    return {
        veiculoPlaca: document.getElementById("veiculoPlaca").value.trim(),
        mecanicoNome: document.getElementById("mecanicoNome").value.trim(),
        kmAtual: document.getElementById("kmAtual").value,
        proxRevisaoKm: document.getElementById("proxRevisaoKm").value,
        dataHora: document.getElementById("dataHora").value,
        status: document.getElementById("status").value
    };
}

function agendarRevisao() {
    const inst = lerCampos();

    const faltando = Object.keys(inst).some(function (k) { return inst[k] === ""; });
    if (faltando) {
        alert("Preencha todos os campos.");
        return;
    }

    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(inst)
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao agendar revisão!"));
            alert("Revisão agendada com sucesso!");
        })
        .catch(err => alert("Erro: " + err.message));
}

/* ---------- Blocos de revisão ---------- */

function campo(rotulo, valor) {
    const texto = (valor === null || valor === undefined || valor === "") ? "-" : valor;
    return '<div class="campo">' +
               '<span class="campo-rotulo">' + rotulo + '</span>' +
               '<span class="campo-valor">' + escapar(texto) + '</span>' +
           '</div>';
}

function blocoRevisao(r) {
    const id = escapar(r.id);
    return '<div class="bloco">' +
               '<div class="bloco-topo">' +
                   '<div class="avatar">🔧</div>' +
                   '<div class="bloco-titulo">' + escapar(placaDaRevisao(r)) + '</div>' +
               '</div>' +
               '<span class="selo">' + escapar(formatarStatus(r.status)) + '</span>' +
               '<div class="campos">' +
                   campo("Mecânico", nomeDoMecanico(r)) +
                   campo("Data", formatarData(r.dataHora)) +
                   campo("KM atual", formatarKm(r.kmAtual)) +
                   campo("Próxima revisão", formatarKm(r.proxRevisaoKm)) +
               '</div>' +
               '<div class="acoes-coluna">' +
                   '<div class="status-linha">' +
                       '<select id="status-' + id + '">' + opcoesStatus(r.status) + '</select>' +
                       '<button data-id="' + id + '" onclick="atualizarStatus(this.dataset.id)">Atualizar status</button>' +
                   '</div>' +
                   '<button class="btn-secundario" data-id="' + id + '" onclick="passarOrcamento(this.dataset.id)">Passar orçamento</button>' +
               '</div>' +
           '</div>';
}

/* ---------- Listar ---------- */

function buscarRevisoes() {
    fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(res => {
            if (!res.ok) throw new Error("Servidor respondeu " + res.status);
            return res.json();
        })
        .then(lista => exibirRevisoes(lista))
        .catch(err => {
            document.getElementById("divPrincipal").textContent = "Não foi possível carregar as revisões.";
            alert("Erro: " + err.message);
        });
}

function exibirRevisoes(lista) {
    const div = document.getElementById("divPrincipal");
    revisoesCarregadas = lista || [];

    if (revisoesCarregadas.length === 0) {
        div.textContent = "Nenhuma revisão agendada.";
        return;
    }

    let html = '<div class="grade">';
    for (let i = 0; i < revisoesCarregadas.length; i++) {
        html += blocoRevisao(revisoesCarregadas[i]);
    }
    html += '</div>';
    div.innerHTML = html;
}

/* ---------- Buscar (por placa) ---------- */

function buscarRevisao() {
    const placa = document.getElementById("placaBusca").value.trim();

    if (placa === "") {
        alert("Digite a placa do veículo.");
        return;
    }

    consultarPorPlaca(placa)
        .then(exibirResultado)
        .catch(err => {
            revisoesCarregadas = [];
            document.getElementById("resultado").innerHTML = "";
            alert("Erro: " + err.message);
        });
}

// O servidor pode devolver uma revisão ou uma lista delas
function exibirResultado(dado) {
    revisoesCarregadas = Array.isArray(dado) ? dado : [dado];

    let html = '<div class="grade">';
    for (let i = 0; i < revisoesCarregadas.length; i++) {
        html += blocoRevisao(revisoesCarregadas[i]);
    }
    html += '</div>';
    document.getElementById("resultado").innerHTML = html;
}

/* ---------- Atualizar status (PUT) ---------- */

function atualizarStatus(id) {
    const revisao = revisoesCarregadas.find(function (r) { return String(r.id) === String(id); });

    if (!revisao) {
        alert("Revisão não encontrada na tela. Recarregue a página.");
        return;
    }

    const novoStatus = document.getElementById("status-" + id).value;

    if (novoStatus === revisao.status) {
        alert("A revisão já está com esse status.");
        return;
    }

    // O PUT recebe a revisão completa, então reenviamos todos os campos com o novo status
    const dto = {
        veiculoPlaca: placaDaRevisao(revisao),
        mecanicoNome: nomeDoMecanico(revisao),
        kmAtual: revisao.kmAtual,
        proxRevisaoKm: revisao.proxRevisaoKm,
        dataHora: dataIso(revisao.dataHora),
        status: novoStatus
    };

    if (!dto.veiculoPlaca || !dto.mecanicoNome || !dto.dataHora) {
        alert("Não foi possível montar os dados da revisão: placa, mecânico ou data não vieram na resposta do servidor.");
        return;
    }

    fetch(url + "/id/" + id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(dto)
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao atualizar status!"));
            alert("Status atualizado com sucesso");
            recarregarTela(dto.veiculoPlaca);
        })
        .catch(err => alert("Erro: " + err.message));
}

function recarregarTela(placa) {
    if (document.getElementById("divPrincipal")) {
        buscarRevisoes();
    } else if (document.getElementById("resultado")) {
        consultarPorPlaca(placa).then(exibirResultado).catch(err => alert("Erro: " + err.message));
    }
}

/* ---------- Passar orçamento (funcionalidade futura) ---------- */

function passarOrcamento(id) {
    // TODO: implementar quando o fluxo de orçamento estiver pronto
    alert("Passar orçamento ainda não está disponível. Essa funcionalidade será adicionada em breve.");
}