const url = "http://localhost:8080/oficina/mecanico";

const headers = {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
          };

function cadastrarMecanico() {
	var inst = {};
	inst.nome = document.getElementById("nome").value;
	inst.telefone = document.getElementById("telefone").value;
	inst.cpf = document.getElementById("cpf").value;
	inst.endereco = document.getElementById("endereco").value;
    inst.salario = document.getElementById("salario").value;
    inst.especialidade = document.getElementById("especialidade").value;

    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(inst)
    })
    	.then(res => res.json())
		.then(res => alert("Mecânico cadastrado com sucesso!"))

    .catch(err => alert("Erro:" + err.message))
}

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
		.catch(err => alert("Erro: " + err.message));
}

function exibirMecanicos(instList) {
	var tabela = "<table>"
	   + "<tr><th>Mecânico</th><th>Telefone</th><th>CPF</th><th>Endereco</th><th>Salario</th><th>Especialidade</th>"
	   + "<th colSpan=4>Ações</th></tr>";
	for (var i = 0; i < instList.length; i++) {
		var inst = instList[i];
		var linha = "<tr>" +
			           "<td>"+ inst.nome + "</td>" +
					   "<td>" + inst.telefone + "</td>" +
					   "<td>" + inst.cpf + "</td>" +
                       "<td>" + inst.endereco + "</td>" +
                       "<td>" + inst.salario + "</td>" +
                       "<td>" + inst.especialidade + "</td>" +
                       '<td><button onclick="window.location.href=\'AtualizarMecanico.html?id=' + inst.id + '\'">Atualizar</button></td>' +
			           '<td><button onclick="deletarMecanico(' + inst.id + ')">Excluir</button></td>' +
					"</tr>";

		tabela += linha;
	}
	tabela +="</table>";
	document.getElementById("divPrincipal").innerHTML = tabela;
}

function buscarMecanico() {
	const id = document.getElementById("id").value.trim();

	if (id == null) {
		alert("ID não encontrado.");
		return;
	}

	fetch(url + "/" + id, {
		method: "GET",
		headers: headers
	})
		.then(res => res.json())
		.then(res => exibirMecanico(res))
		.catch(err => alert("Erro: " + err.message));
}

function carregarMecanico() {
	const id = document.getElementById("id").value.trim();

	if (id === "") {
		alert("Digite o id do cliente.");
		return;
	}

	fetch(url + "/" + id, {
		method: "GET",
		headers: { "Accept": "application/json" }
	})
		.then(res => res.json())
		.then(res => preencherCampos(res))
		.catch(err => alert("Erro: " + err.message));
}

function exibirMecanico(mecanico) {
    document.getElementById("resultado").innerHTML =
        "<p><b>Nome:</b> " + mecanico.nome + "</p>" +
        "<p><b>Telefone:</b> " + mecanico.telefone + "</p>" +
        "<p><b>CPF:</b> " + mecanico.cpf + "</p>" +
        "<p><b>Endereço:</b> " + mecanico.endereco + "</p>" +
        "<p><b>Salario:</b> " + mecanico.salario + "</p>" +
        "<p><b>Especialidade:</b> " + mecanico.especialidade + "</p>";
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
    	const id = document.getElementById("id").value.trim();

	if (id == null) {
		alert("ID não encontrado.");
		return;
	}

	var inst = {};
	inst.nome = document.getElementById("nome").value;
	inst.telefone = document.getElementById("telefone").value;
	inst.cpf = document.getElementById("cpf").value;
	inst.endereco = document.getElementById("endereco").value;
    inst.salario = document.getElementById("salario").value;
    inst.especialidade = document.getElementById("especialidade").value;

    fetch(url + "/" + id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(inst)
    })
    .then(res => alert("Atualizado com sucesso"))
    .then(res => exibirMecanico(res))
    .catch(err => alert("Erro:" + err.message))
}

function irParaAtualizar() {
	const id = document.getElementById("id").value.trim();

	if (id === "") {
		alert("Digite o id do mecânico e clique em Buscar primeiro.");
		return;
	}

	window.location.href = "AtualizarMecanico.html?id=" + encodeURIComponent(id);
}

function carregarMecanicoDaUrl() {
	const id = new URLSearchParams(window.location.search).get("id");

	if (id === null) {
		return; 
	}

	document.getElementById("id").value = id;
	carregarMecanico();
}

function deletarMecanico(id) {
	if (id == null) {
		alert("ID não encontrado.");
		return;
	}

	fetch(url + "/" + id, {
            headers: headers,
            method: "DELETE"
        })
        .then(res => alert("Excluído com sucesso"))
        .catch(err => alert("Erro:" + err.message))
}
