const url = "http://localhost:8080/oficina/veiculo";

const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
};

// Guarda o veículo carregado pela busca de placa (precisamos do id para atualizar/excluir)
let veiculoAtual = null;

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
        marca: document.getElementById("marca").value,
        modelo: document.getElementById("modelo").value,
        ano: document.getElementById("ano").value,
        cor: document.getElementById("cor").value,
        placa: document.getElementById("placa").value,
        clienteCpf: document.getElementById("clienteCpf").value,
        tipoVeiculo: document.getElementById("tipoVeiculo").value
    };
}

function lerPlacaBusca() {
    return document.getElementById("placaBusca").value.trim();
}

// O CPF do cliente pode vir direto no veículo ou dentro de um objeto "cliente"
function cpfDoCliente(veiculo) {
    return veiculo.clienteCpf ?? (veiculo.cliente && veiculo.cliente.cpf) ?? "";
}

// CARRO -> Carro
function formatarTipo(tipo) {
    if (!tipo) return tipo;
    const t = String(tipo);
    return t.charAt(0).toUpperCase() + t.slice(1).toLowerCase();
}

function iconeTipo(tipo) {
    return { CARRO: "🚗", MOTO: "🏍️", CAMIONETE: "🛻" }[String(tipo).toUpperCase()] || "🚘";
}

// Busca um veículo na API pela placa (GET /placa/{placa})
async function consultarPorPlaca(placa) {
    const res = await fetch(url + "/placa/" + encodeURIComponent(placa), {
        method: "GET",
        headers: { "Accept": "application/json" }
    });
    if (res.status === 404) throw new Error("Veículo não encontrado.");
    if (!res.ok) throw new Error(await lerErro(res, "Servidor respondeu " + res.status));

    const texto = await res.text();
    if (!texto) throw new Error("Veículo não encontrado."); // servidor respondeu sem corpo
    return JSON.parse(texto);
}

/* ---------- Cadastrar ---------- */

function cadastrarVeiculo() {
    const inst = lerCampos();

    if (inst.tipoVeiculo === "") {
        alert("Selecione o tipo de veículo.");
        return;
    }

    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(inst)
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao cadastrar veículo!"));
            alert("Veículo cadastrado com sucesso!");
        })
        .catch(err => alert("Erro: " + err.message));
}

/* ---------- Blocos de veículo ---------- */

function campo(rotulo, valor) {
    const texto = (valor === null || valor === undefined || valor === "") ? "-" : valor;
    return '<div class="campo">' +
               '<span class="campo-rotulo">' + rotulo + '</span>' +
               '<span class="campo-valor">' + escapar(texto) + '</span>' +
           '</div>';
}

function blocoVeiculo(v) {
    const titulo = ((v.marca || "") + " " + (v.modelo || "")).trim();
    return '<div class="bloco">' +
               '<div class="bloco-topo">' +
                   '<div class="avatar">' + iconeTipo(v.tipoVeiculo) + '</div>' +
                   '<div class="bloco-titulo">' + escapar(titulo) + '</div>' +
               '</div>' +
               '<div class="campos">' +
                   campo("Placa", v.placa) +
                   campo("Tipo", formatarTipo(v.tipoVeiculo)) +
                   campo("Ano", v.ano) +
                   campo("Cor", v.cor) +
                   campo("CPF do cliente", cpfDoCliente(v)) +
               '</div>' +
               '<div class="acoes">' +
                   '<button data-placa="' + escapar(v.placa) + '" onclick="irParaAtualizar(this.dataset.placa)">Atualizar</button>' +
                   '<button data-id="' + escapar(v.id) + '" onclick="deletarVeiculo(this.dataset.id)">Excluir</button>' +
               '</div>' +
           '</div>';
}

/* ---------- Listar ---------- */

function buscarVeiculos() {
    fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(res => {
            if (!res.ok) throw new Error("Servidor respondeu " + res.status);
            return res.json();
        })
        .then(lista => exibirVeiculos(lista))
        .catch(err => {
            document.getElementById("divPrincipal").textContent = "Não foi possível carregar os veículos.";
            alert("Erro: " + err.message);
        });
}

function exibirVeiculos(instList) {
    const div = document.getElementById("divPrincipal");

    if (!instList || instList.length === 0) {
        div.textContent = "Nenhum veículo cadastrado.";
        return;
    }

    let html = '<div class="grade">';
    for (let i = 0; i < instList.length; i++) {
        html += blocoVeiculo(instList[i]);
    }
    html += '</div>';
    div.innerHTML = html;
}

/* ---------- Buscar (por placa) ---------- */

function buscarVeiculo() {
    const placa = lerPlacaBusca();

    if (placa === "") {
        alert("Digite a placa do veículo.");
        return;
    }

    consultarPorPlaca(placa)
        .then(veiculo => {
            veiculoAtual = veiculo;
            exibirVeiculo(veiculo);
        })
        .catch(err => {
            veiculoAtual = null;
            document.getElementById("resultado").innerHTML = "";
            alert("Erro: " + err.message);
        });
}

function exibirVeiculo(veiculo) {
    document.getElementById("resultado").innerHTML = blocoVeiculo(veiculo);
}

/* ---------- Atualizar (por placa) ---------- */

function carregarVeiculo() {
    const placa = lerPlacaBusca();

    if (placa === "") {
        alert("Digite a placa do veículo.");
        return;
    }

    consultarPorPlaca(placa)
        .then(veiculo => {
            veiculoAtual = veiculo;
            preencherCampos(veiculo);
        })
        .catch(err => {
            veiculoAtual = null;
            alert("Erro: " + err.message);
        });
}

function preencherCampos(veiculo) {
    document.getElementById("marca").value = veiculo.marca ?? "";
    document.getElementById("modelo").value = veiculo.modelo ?? "";
    document.getElementById("ano").value = veiculo.ano ?? "";
    document.getElementById("cor").value = veiculo.cor ?? "";
    document.getElementById("placa").value = veiculo.placa ?? "";
    document.getElementById("clienteCpf").value = cpfDoCliente(veiculo);
    document.getElementById("tipoVeiculo").value = veiculo.tipoVeiculo ?? "";
}

function atualizarVeiculo() {
    if (veiculoAtual === null || veiculoAtual.id === undefined) {
        alert("Busque o veículo pela placa antes de atualizar.");
        return;
    }

    const inst = lerCampos();

    if (inst.tipoVeiculo === "") {
        alert("Selecione o tipo de veículo.");
        return;
    }

    fetch(url + "/id/" + veiculoAtual.id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(inst)
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao atualizar veículo!"));
            veiculoAtual = Object.assign({}, veiculoAtual, inst);
            alert("Atualizado com sucesso");
        })
        .catch(err => alert("Erro: " + err.message));
}

function irParaAtualizar(placa) {
    const alvo = placa || (veiculoAtual && veiculoAtual.placa);

    if (!alvo) {
        alert("Busque o veículo pela placa primeiro.");
        return;
    }

    window.location.href = "AtualizarVeiculo.html?placa=" + encodeURIComponent(alvo);
}

function carregarVeiculoDaUrl() {
    const placa = new URLSearchParams(window.location.search).get("placa");

    if (placa === null) {
        return;
    }

    document.getElementById("placaBusca").value = placa;
    carregarVeiculo();
}

/* ---------- Excluir ---------- */

function deletarVeiculo(id) {
    if (id === undefined || id === null || id === "") {
        alert("ID não encontrado.");
        return;
    }

    if (!confirm("Deseja realmente excluir este veículo?")) {
        return;
    }

    fetch(url + "/" + id, {
        headers: headers,
        method: "DELETE"
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao excluir veículo!"));
            alert("Excluído com sucesso");
            veiculoAtual = null;

            if (document.getElementById("divPrincipal")) buscarVeiculos();

            const resultado = document.getElementById("resultado");
            if (resultado) resultado.innerHTML = "";
        })
        .catch(err => alert("Erro: " + err.message));
}