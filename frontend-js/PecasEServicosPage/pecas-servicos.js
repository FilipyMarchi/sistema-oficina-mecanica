const BASE = "http://localhost:8080/oficina";

const headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json',
};

// >>> Ajuste com os valores do seu enum TipoFornecedor <<<
const TIPOS_FORNECEDOR = ["PECAS", "FLUIDOS", "SERVICOS"];

/* ------------------------------------------------------------------
   Configuração de cada tela. Para mudar um campo, rótulo ou texto,
   edite apenas aqui: formulário, blocos e envio saem desta lista.
   tipo: "text" | "number" | "date" | "select" (com opcoes: [...])
   opcional: o campo pode ficar vazio e não é enviado ao servidor
   moeda: mostra como R$
   naTitulo: o campo aparece no título do bloco, não na lista de campos
   ler: função opcional para achar o valor quando o JSON vem aninhado
------------------------------------------------------------------- */
const CONFIG = {
    peca: {
        rota: "/peca",
        titulo: "Peças",
        icone: "🔩",
        novo: "Nova peça",
        cadastrar: "Cadastrar peça",
        atualizar: "Atualizar peça",
        vazio: "Nenhuma peça cadastrada.",
        confirmaExcluir: "Deseja realmente excluir esta peça?",
        campos: [
            { nome: "nome", rotulo: "Nome", tipo: "text", naTitulo: true },
            { nome: "valor", rotulo: "Valor", tipo: "number", passo: "0.01", moeda: true },
            { nome: "quantidade", rotulo: "Quantidade", tipo: "number", passo: "1" },
            { nome: "fornecedorNome", rotulo: "Fornecedor", tipo: "text",
              ler: function (r) { return r.fornecedorNome ?? (r.fornecedor && r.fornecedor.nome); } },
            { nome: "garantiaValidade", rotulo: "Validade da garantia", tipo: "date" }
        ]
    },
    fluido: {
        rota: "/fluido",
        titulo: "Fluidos",
        icone: "🛢️",
        novo: "Novo fluido",
        cadastrar: "Cadastrar fluido",
        atualizar: "Atualizar fluido",
        vazio: "Nenhum fluido cadastrado.",
        confirmaExcluir: "Deseja realmente excluir este fluido?",
        campos: [
            { nome: "marca", rotulo: "Marca", tipo: "text", naTitulo: true },
            { nome: "tipo", rotulo: "Tipo", tipo: "text", naTitulo: true },
            { nome: "validade", rotulo: "Validade", tipo: "date" },
            { nome: "quantidade", rotulo: "Quantidade", tipo: "number", passo: "1" },
            { nome: "fornecedorNome", rotulo: "Fornecedor", tipo: "text",
              ler: function (r) { return r.fornecedorNome ?? (r.fornecedor && r.fornecedor.nome); } }
        ]
    },
    servico: {
        rota: "/servico",
        titulo: "Serviços",
        icone: "🛠️",
        novo: "Novo serviço",
        cadastrar: "Cadastrar serviço",
        atualizar: "Atualizar serviço",
        vazio: "Nenhum serviço cadastrado.",
        confirmaExcluir: "Deseja realmente excluir este serviço?",
        campos: [
            { nome: "tipoServico", rotulo: "Tipo de serviço", tipo: "text", naTitulo: true },
            { nome: "valor", rotulo: "Valor", tipo: "number", passo: "0.01", moeda: true },
            { nome: "garantiaValidade", rotulo: "Validade da garantia", tipo: "date" }
        ]
    },
    fornecedor: {
        rota: "/fornecedor",
        titulo: "Fornecedores",
        icone: "📦",
        novo: "Novo fornecedor",
        cadastrar: "Cadastrar fornecedor",
        atualizar: "Atualizar fornecedor",
        vazio: "Nenhum fornecedor cadastrado.",
        confirmaExcluir: "Deseja realmente excluir este fornecedor?",
        campos: [
            { nome: "nome", rotulo: "Nome", tipo: "text", naTitulo: true },
            { nome: "telefone", rotulo: "Telefone", tipo: "text" },
            { nome: "tipo", rotulo: "Tipo", tipo: "select", opcoes: TIPOS_FORNECEDOR, opcional: true }
        ]
    }
};

let cfg = null;          // configuração da tela aberta
let registros = [];      // itens que estão na tela
let editandoId = null;   // id do item em edição (null = cadastrando)

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

function urlBase() {
    return BASE + cfg.rota;
}

function valorDe(registro, campo) {
    return campo.ler ? campo.ler(registro) : registro[campo.nome];
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

function formatarMoeda(v) {
    const n = Number(v);
    if (v === null || v === undefined || v === "" || isNaN(n)) return v;
    return n.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

// EM_ANDAMENTO -> Em andamento
function formatarEnum(v) {
    const t = String(v).replace(/_/g, " ").toLowerCase();
    return t.charAt(0).toUpperCase() + t.slice(1);
}

function formatarCampo(campo, valor) {
    if (valor === null || valor === undefined || valor === "") return "-";
    if (campo.moeda) return formatarMoeda(valor);
    if (campo.tipo === "select") return formatarEnum(valor);
    if (campo.tipo === "date") return formatarData(valor);
    return valor;
}

/* ---------- Inicialização da tela ---------- */

function iniciarTela(chave) {
    cfg = CONFIG[chave];
    document.title = cfg.titulo;
    document.getElementById("tituloPagina").textContent = cfg.titulo;
    document.getElementById("btnNovo").textContent = "+ " + cfg.novo;
    montarFormulario();
    carregarLista();
}

function montarFormulario() {
    let html = "";
    cfg.campos.forEach(function (c) {
        const rotulo = c.rotulo + (c.opcional ? " (opcional)" : "");
        let entrada;

        if (c.tipo === "select") {
            entrada = '<select id="campo-' + c.nome + '">' +
                      '<option value="">' + (c.opcional ? "Não informado" : "Selecione...") + '</option>' +
                      c.opcoes.map(function (o) {
                          return '<option value="' + escapar(o) + '">' + escapar(formatarEnum(o)) + '</option>';
                      }).join("") +
                      '</select>';
        } else {
            entrada = '<input id="campo-' + c.nome + '" type="' + c.tipo + '"' +
                      (c.tipo === "number" ? ' min="0" step="' + (c.passo || "1") + '"' : "") +
                      ' value="">';
        }

        html += '<label for="campo-' + c.nome + '">' + escapar(rotulo) + '</label>' + entrada;
    });
    document.getElementById("camposForm").innerHTML = html;
}

/* ---------- Formulário (cadastrar / atualizar) ---------- */

function abrirFormulario(registro) {
    editandoId = registro ? registro.id : null;

    cfg.campos.forEach(function (c) {
        const el = document.getElementById("campo-" + c.nome);
        if (!registro) {
            el.value = "";
            return;
        }
        const v = valorDe(registro, c);
        el.value = c.tipo === "date" ? dataIso(v) : (v ?? "");
    });

    document.getElementById("formTitulo").textContent = registro ? cfg.atualizar : cfg.cadastrar;
    document.getElementById("btnSalvar").textContent = registro ? "Salvar alterações" : "Cadastrar";

    const painel = document.getElementById("painelForm");
    painel.classList.remove("oculto");
    painel.scrollIntoView({ behavior: "smooth", block: "center" });
    document.getElementById("campo-" + cfg.campos[0].nome).focus({ preventScroll: true });
}

function fecharFormulario() {
    editandoId = null;
    document.getElementById("painelForm").classList.add("oculto");
}

function lerFormulario() {
    const dto = {};

    for (let i = 0; i < cfg.campos.length; i++) {
        const c = cfg.campos[i];
        const bruto = document.getElementById("campo-" + c.nome).value.trim();

        if (bruto === "") {
            if (c.opcional) continue;
            alert('Preencha o campo "' + c.rotulo + '".');
            return null;
        }

        if (c.tipo === "number") {
            const n = Number(bruto);
            if (isNaN(n) || n < 0) {
                alert('O campo "' + c.rotulo + '" precisa ser um número válido.');
                return null;
            }
            dto[c.nome] = n;
        } else {
            dto[c.nome] = bruto;
        }
    }

    return dto;
}

function salvar() {
    const dto = lerFormulario();
    if (dto === null) return;

    const editando = editandoId !== null;
    const endereco = editando ? urlBase() + "/" + editandoId : urlBase();

    fetch(endereco, {
        headers: headers,
        method: editando ? "PUT" : "POST",
        body: JSON.stringify(dto)
    })
        .then(async res => {
            if (!res.ok) {
                throw new Error(await lerErro(res, editando ? "Erro ao atualizar!" : "Erro ao cadastrar!"));
            }
            alert(editando ? "Atualizado com sucesso" : "Cadastrado com sucesso!");
            fecharFormulario();
            carregarLista();
        })
        .catch(err => alert("Erro: " + err.message));
}

/* ---------- Listar ---------- */

function carregarLista() {
    fetch(urlBase(), {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(res => {
            if (!res.ok) throw new Error("Servidor respondeu " + res.status);
            return res.json();
        })
        .then(lista => exibirLista(lista))
        .catch(err => {
            document.getElementById("divPrincipal").textContent = "Não foi possível carregar a lista.";
            alert("Erro: " + err.message);
        });
}

function exibirLista(lista) {
    const div = document.getElementById("divPrincipal");
    registros = lista || [];

    if (registros.length === 0) {
        div.textContent = cfg.vazio;
        return;
    }

    let html = '<div class="grade">';
    for (let i = 0; i < registros.length; i++) {
        html += blocoRegistro(registros[i]);
    }
    html += '</div>';
    div.innerHTML = html;
}

function campoHtml(rotulo, valor) {
    return '<div class="campo">' +
               '<span class="campo-rotulo">' + escapar(rotulo) + '</span>' +
               '<span class="campo-valor">' + escapar(valor) + '</span>' +
           '</div>';
}

function blocoRegistro(r) {
    const partesTitulo = [];
    let camposHtml = "";

    cfg.campos.forEach(function (c) {
        const v = valorDe(r, c);
        if (c.naTitulo) {
            if (v !== null && v !== undefined && v !== "") partesTitulo.push(v);
        } else {
            camposHtml += campoHtml(c.rotulo, formatarCampo(c, v));
        }
    });

    const id = escapar(r.id);
    return '<div class="bloco">' +
               '<div class="bloco-topo">' +
                   '<div class="avatar">' + cfg.icone + '</div>' +
                   '<div class="bloco-titulo">' + escapar(partesTitulo.join(" · ")) + '</div>' +
               '</div>' +
               '<div class="campos">' + camposHtml + '</div>' +
               '<div class="acoes">' +
                   '<button data-id="' + id + '" onclick="editar(this.dataset.id)">Atualizar</button>' +
                   '<button data-id="' + id + '" onclick="excluir(this.dataset.id)">Excluir</button>' +
               '</div>' +
           '</div>';
}

/* ---------- Atualizar e Excluir ---------- */

function editar(id) {
    const registro = registros.find(function (r) { return String(r.id) === String(id); });

    if (!registro) {
        alert("Item não encontrado na tela. Recarregue a página.");
        return;
    }

    abrirFormulario(registro);
}

function excluir(id) {
    if (id === undefined || id === null || id === "") {
        alert("ID não encontrado.");
        return;
    }

    if (!confirm(cfg.confirmaExcluir)) {
        return;
    }

    fetch(urlBase() + "/" + id, {
        headers: headers,
        method: "DELETE"
    })
        .then(async res => {
            if (!res.ok) throw new Error(await lerErro(res, "Erro ao excluir!"));
            alert("Excluído com sucesso");
            if (editandoId !== null && String(editandoId) === String(id)) fecharFormulario();
            carregarLista();
        })
        .catch(err => alert("Erro: " + err.message));
}
