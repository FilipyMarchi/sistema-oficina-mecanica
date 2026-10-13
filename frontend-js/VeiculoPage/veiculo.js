const url = "http://localhost:8080/oficina/veiculo";

const headers = {
              'Accept': 'application/json',
              'Content-Type': 'application/json',
          };

function cadastrarVeiculo() {
	var inst = {};
	inst.marca = document.getElementById("marca").value;
	inst.modelo = document.getElementById("modelo").value;
	inst.ano = document.getElementById("ano").value;
	inst.cor = document.getElementById("cor").value;
    inst.placa = document.getElementById("placa").value;
    inst.clienteId = document.getElementById("clienteId").value;
    inst.tipoVeiculo = document.getElementById("tipoVeiculo").value;

    fetch(url, {
        headers: headers,
        method: "POST",
        body: JSON.stringify(inst)
    })
    	.then(res => res.json())
		.then(res => alert("Veículo cadastrado com sucesso!"))

    .catch(err => alert("Erro:" + err.message))
}

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
		.catch(err => alert("Erro: " + err.message));
}

function buscarVeiculo() {
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
		.then(res => exibirVeiculo(res))
		.catch(err => alert("Erro: " + err.message));
}

function exibirVeiculos(instList) {
	var tabela = "<table>"
	   + "<tr><th>Marca</th><th>Modelo</th><th>Ano</th><th>Cor</th><th>Placa</th><th>Cliente</th><th>Tipo</th>"
	   + "<th colSpan=3>Ações</th></tr>";
	for (var i = 0; i < instList.length; i++) {
		var inst = instList[i];
		var linha = "<tr>" +
			           "<td>"+ inst.marca + "</td>" +
					   "<td>" + inst.modelo + "</td>" +
					   "<td>" + inst.ano + "</td>" +
                       "<td>" + inst.cor + "</td>" +
                       "<td>" + inst.placa + "</td>" +
                       "<td>" + inst.clienteId + "</td>" +
                       "<td>" + inst.tipoVeiculo + "</td>" +
                       '<td><button onclick="window.location.href=\'AtualizarVeiculo.html?id=' + inst.id + '\'">Atualizar</button></td>' +
			           '<td><button onclick="deletarVeiculo(' + inst.id + ')">Excluir</button></td>' +
					"</tr>";

		tabela += linha;
	}
	tabela +="</table>";
	document.getElementById("divPrincipal").innerHTML = tabela;
}

function carregarVeiculo() {
	const id = document.getElementById("id").value.trim();

	if (id === "") {
		alert("Digite o id do veículo.");
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

function exibirVeiculo(veiculo) {
    document.getElementById("resultado").innerHTML =
        "<p><b>Marca:</b> " + veiculo.marca + "</p>" +
        "<p><b>Modelo:</b> " + veiculo.modelo + "</p>" +
        "<p><b>Ano:</b> " + veiculo.ano + "</p>" +
        "<p><b>Cor:</b> " + veiculo.cor + "</p>" +
        "<p><b>Placa:</b> " + veiculo.placa + "</p>" +
        "<p><b>Cliente:</b> " + veiculo.clienteId + "</p>" +
        "<p><b>Tipo:</b> " + veiculo.tipoVeiculo + "</p>";
}

function preencherCampos(veiculo) {
	document.getElementById("marca").value = veiculo.marca ?? "";
	document.getElementById("modelo").value = veiculo.modelo ?? "";
	document.getElementById("ano").value = veiculo.ano ?? "";
	document.getElementById("cor").value = veiculo.cor ?? "";
	document.getElementById("placa").value = veiculo.placa ?? "";
	document.getElementById("clienteId").value = veiculo.clienteId ?? "";
    document.getElementById("tipoVeiculo").value = veiculo.tipoVeiculo ?? "";
}

function atualizarVeiculo() {
    	const id = document.getElementById("id").value.trim();

	if (id == null) {
		alert("ID não encontrado.");
		return;
	}

	var inst = {};
	inst.marca = document.getElementById("marca").value;
	inst.modelo = document.getElementById("modelo").value;
	inst.ano = document.getElementById("ano").value;
	inst.cor = document.getElementById("cor").value;
    inst.placa = document.getElementById("placa").value;
    inst.clienteId = document.getElementById("clienteId").value;
    inst.tipoVeiculo = document.getElementById("tipoVeiculo").value;

    fetch(url + "/" + id, {
        headers: headers,
        method: "PUT",
        body: JSON.stringify(inst)
    })
    .then(res => alert("Atualizado com sucesso"))
    .then(res => exibirVeiculo(res))
    .catch(err => alert("Erro:" + err.message))
}

function irParaAtualizar() {
	const id = document.getElementById("id").value.trim();

	if (id === "") {
		alert("Digite o id do veiculo e clique em Buscar primeiro.");
		return;
	}

	window.location.href = "AtualizarVeiculo.html?id=" + encodeURIComponent(id);
}

function carregarVeiculoDaUrl() {
	const id = new URLSearchParams(window.location.search).get("id");

	if (id === null) {
		return; 
	}

	document.getElementById("id").value = id;
	carregarVeiculo();
}

function deletarVeiculo() {
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