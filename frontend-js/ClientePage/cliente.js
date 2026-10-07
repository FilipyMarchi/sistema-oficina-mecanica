const url = "http://localhost:8080/oficina/cliente";

const headers = {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
          };

function cadastrarCliente() {
	var inst = {};
	inst.nome = document.getElementById("nome").value;
	inst.telefone = document.getElementById("telefone").value;
	inst.cpf = document.getElementById("cpf").value;
	inst.endereco = document.getElementById("endereco").value;
    inst.email = document.getElementById("email").value;
    inst.senha = document.getElementById("senha").value;

    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(inst)
    })
    	.then(res => res.json())
		.then(res => alert("Cliente cadastrado com sucesso!"))

    .catch(err => alert("Erro:" + err.message))
}

function buscarClientes() {
	fetch(url, {
		method: "GET",
		headers: { "Accept": "application/json" }
	})
		.then(res => {
			if (!res.ok) throw new Error("Servidor respondeu " + res.status);
			return res.json();
		})
		.then(lista => exibirClientes(lista))
		.catch(err => alert("Erro: " + err.message));
}

function exibirClientes(instList) {
	var tabela = "<table>"
	   + "<tr><th>Cliente</th><th>Telefone</th><th>CPF</th><th>Endereco</th><th>E-mail</th>"
	   + "<th colSpan=3>Ações</th></tr>";
	for (var i = 0; i < instList.length; i++) {
		var inst = instList[i];
		var linha = "<tr>" +
			           "<td>"+ inst.nome + "</td>" +
					   "<td>" + inst.telefone + "</td>" +
					   "<td>" + inst.cpf + "</td>" +
                       "<td>" + inst.endereco + "</td>" +
                       "<td>" + inst.email + "</td>" +
                       '<td><button onclick="window.location.href=\'AtualizarCliente.html?id=' + inst.id + '\'">Atualizar</button></td>' +
			           '<td><button onclick="deletarCliente(' + inst.id + ')">Excluir</button></td>' +
					"</tr>";

		tabela += linha;
	}
	tabela +="</table>";
	document.getElementById("divPrincipal").innerHTML = tabela;
}

function buscarCliente() {
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
		.then(res => exibirCliente(res))
		.catch(err => alert("Erro: " + err.message));
}

function carregarCliente() {
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

function exibirCliente(cliente) {
    document.getElementById("resultado").innerHTML =
        "<p><b>Nome:</b> " + cliente.nome + "</p>" +
        "<p><b>Telefone:</b> " + cliente.telefone + "</p>" +
        "<p><b>CPF:</b> " + cliente.cpf + "</p>" +
        "<p><b>Endereço:</b> " + cliente.endereco + "</p>" +
        "<p><b>Email:</b> " + cliente.email + "</p>";
}

function preencherCampos(cliente) {
	document.getElementById("nome").value = cliente.nome ?? "";
	document.getElementById("telefone").value = cliente.telefone ?? "";
	document.getElementById("cpf").value = cliente.cpf ?? "";
	document.getElementById("endereco").value = cliente.endereco ?? "";
	document.getElementById("email").value = cliente.email ?? "";
	document.getElementById("senha").value = cliente.senha ?? "";
}

function atualizarCliente() {
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
    inst.email = document.getElementById("email").value;
    inst.senha = document.getElementById("senha").value;

    fetch(url + "/" + id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(inst)
    })
    .then(res => alert("Atualizado com sucesso"))
    .then(res => exibirCliente(res))
    .catch(err => alert("Erro:" + err.message))
}

function irParaAtualizar() {
	const id = document.getElementById("id").value.trim();

	if (id === "") {
		alert("Digite o id do cliente e clique em Buscar primeiro.");
		return;
	}

	window.location.href = "AtualizarCliente.html?id=" + encodeURIComponent(id);
}

function carregarClienteDaUrl() {
	const id = new URLSearchParams(window.location.search).get("id");

	if (id === null) {
		return; 
	}

	document.getElementById("id").value = id;
	carregarCliente();
}

function deletarCliente() {
	const id = document.getElementById("id").value.trim();

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