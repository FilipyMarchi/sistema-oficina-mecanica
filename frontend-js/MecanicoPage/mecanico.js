const url = "http://localhost:8080/oficina/mecanico";

const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
};

// Guarda o mecânico carregado pela busca de nome (precisamos do id para atualizar/excluir)
let mecanicoAtual = null;

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

function lerCampos() {
    return {
        nome: document.getElementById("nome").value,
        telefone: document.getElementById("telefone").value,
        cpf: document.getElementById("cpf").value,
        endereco: document.getElementById("endereco").value,
        salario: document.getElementById("salario").value,
        especialidade: document.getElementById("especialidade").value
    };
}

function lerNomeBusca() {
    return document.getElementById("nomeBusca").value.trim();
}

// Busca um mecânico na API pelo nome (GET /nome/{nome})
async function consultarPorNome(nome) {
    const res = await fetch(url + "/nome/" + encodeURIComponent(nome), {
        method: "GET",
        headers: { "Accept": "application/json" }
    });
    if (res.status === 404) throw new Error("Mecânico não encontrado.");
    if (!res.ok) throw new Error(await lerErro(res, "Servidor respondeu " + res.status));

    const texto = await res.text();
    if (!texto) throw new Error("Mecânico não encontrado."); // servidor respondeu sem corpo
    return JSON.parse(texto);
}

function formatarSalario(valor) {
    const n = Number(valor);
    if (valor === null || valor === undefined || valor === "" || isNaN(n)) return valor;
    return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

/* ---------- Cadastrar ---------- */

function cadastrarMecanico() {
    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(lerCampos())
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao cadastrar mecânico!"));
            alert("Mecânico cadastrado com sucesso!");
        })
        .catch(err => alert("Erro: " + err.message));
}

/* ---------- Blocos de mecânico ---------- */

function campo(rotulo, valor) {
    const texto = (valor === null || valor === undefined || valor === "") ? "-" : valor;
    return '<div class="campo">' +
               '<span class="campo-rotulo">' + rotulo + '</span>' +
               '<span class="campo-valor">' + escapar(texto) + '</span>' +
           '</div>';
}

function blocoMecanico(m) {
    const inicial = (m.nome || "?").trim().charAt(0).toUpperCase() || "?";
    return '<div class="bloco">' +
               '<div class="bloco-topo">' +
                   '<div class="avatar">' + escapar(inicial) + '</div>' +
                   '<div class="bloco-titulo">' + escapar(m.nome) + '</div>' +
               '</div>' +
               '<div class="campos">' +
                   campo("Especialidade", m.especialidade) +
                   campo("Salário", formatarSalario(m.salario)) +
                   campo("CPF", m.cpf) +
                   campo("Telefone", m.telefone) +
                   campo("Endereço", m.endereco) +
               '</div>' +
               '<div class="acoes">' +
                   '<button data-nome="' + escapar(m.nome) + '" onclick="irParaAtualizar(this.dataset.nome)">Atualizar</button>' +
                   '<button data-id="' + escapar(m.id) + '" onclick="deletarMecanico(this.dataset.id)">Excluir</button>' +
               '</div>' +
           '</div>';
}

/* ---------- Listar ---------- */

function buscarMecanicos() {
    fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(res => {
            if (!res.ok) throw new Error("Servidor respondeu " + res.status);
            return res.json();
        })
        .then(lista => exibirMecanicos(lista))
        .catch(err => {
            document.getElementById("divPrincipal").textContent = "Não foi possível carregar os mecânicos.";
            alert("Erro: " + err.message);
        });
}

function exibirMecanicos(instList) {
    const div = document.getElementById("divPrincipal");

    if (!instList || instList.length === 0) {
        div.textContent = "Nenhum mecânico cadastrado.";
        return;
    }

    let html = '<div class="grade">';
    for (let i = 0; i < instList.length; i++) {
        html += blocoMecanico(instList[i]);
    }
    html += '</div>';
    div.innerHTML = html;
}

/* ---------- Buscar (por nome) ---------- */

function buscarMecanico() {
    const nome = lerNomeBusca();

    if (nome === "") {
        alert("Digite o nome do mecânico.");
        return;
    }

    consultarPorNome(nome)
        .then(mecanico => {
            mecanicoAtual = mecanico;
            exibirMecanico(mecanico);
        })
        .catch(err => {
            mecanicoAtual = null;
            document.getElementById("resultado").innerHTML = "";
            alert("Erro: " + err.message);
        });
}

function exibirMecanico(mecanico) {
    document.getElementById("resultado").innerHTML = blocoMecanico(mecanico);
}

/* ---------- Atualizar (por nome) ---------- */

function carregarMecanico() {
    const nome = lerNomeBusca();

    if (nome === "") {
        alert("Digite o nome do mecânico.");
        return;
    }

    consultarPorNome(nome)
        .then(mecanico => {
            mecanicoAtual = mecanico;
            preencherCampos(mecanico);
        })
        .catch(err => {
            mecanicoAtual = null;
            alert("Erro: " + err.message);
        });
}

function preencherCampos(mecanico) {
    document.getElementById("nome").value = mecanico.nome ?? "";
    document.getElementById("telefone").value = mecanico.telefone ?? "";
    document.getElementById("cpf").value = mecanico.cpf ?? "";
    document.getElementById("endereco").value = mecanico.endereco ?? "";
    document.getElementById("salario").value = mecanico.salario ?? "";
    document.getElementById("especialidade").value = mecanico.especialidade ?? "";
}

function atualizarMecanico() {
    if (mecanicoAtual === null || mecanicoAtual.id === undefined) {
        alert("Busque o mecânico pelo nome antes de atualizar.");
        return;
    }

    const inst = lerCampos();

    fetch(url + "/id/" + mecanicoAtual.id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(inst)
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao atualizar mecânico!"));
            mecanicoAtual = Object.assign({}, mecanicoAtual, inst);
            alert("Atualizado com sucesso");
        })
        .catch(err => alert("Erro: " + err.message));
}

function irParaAtualizar(nome) {
    const alvo = nome || (mecanicoAtual && mecanicoAtual.nome);

    if (!alvo) {
        alert("Busque o mecânico pelo nome primeiro.");
        return;
    }

    window.location.href = "AtualizarMecanico.html?nome=" + encodeURIComponent(alvo);
}

function carregarMecanicoDaUrl() {
    const nome = new URLSearchParams(window.location.search).get("nome");

    if (nome === null) {
        return;
    }

    document.getElementById("nomeBusca").value = nome;
    carregarMecanico();
}

/* ---------- Excluir ---------- */

function deletarMecanico(id) {
    if (id === undefined || id === null || id === "") {
        alert("ID não encontrado.");
        return;
    }

    if (!confirm("Deseja realmente excluir este mecânico?")) {
        return;
    }

    fetch(url + "/" + id, {
        headers: headers,
        method: "DELETE"
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao excluir mecânico!"));
            alert("Excluído com sucesso");
            mecanicoAtual = null;

            if (document.getElementById("divPrincipal")) buscarMecanicos();

            const resultado = document.getElementById("resultado");
            if (resultado) resultado.innerHTML = "";
        })
        .catch(err => alert("Erro: " + err.message));
}