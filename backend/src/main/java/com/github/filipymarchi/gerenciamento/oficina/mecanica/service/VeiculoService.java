package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.VeiculoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ClienteModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.VeiculoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.ClienteRepository;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public List<VeiculoModel> findAll() {
        return veiculoRepository.findAll();
    }

    public VeiculoModel findById(Integer id) {
        return veiculoRepository.findById(id).orElse(null);
    }

    public VeiculoModel findByPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("Digite a placa!");
        }

        if (!placa.matches("[A-Z]{3}\\d[A-Z0-9]\\d{2}")) {
            throw new IllegalArgumentException(
                    "Placa inválida! Use 3 letras maiúsculas, 1 número, 1 letra ou número e 2 números, sem traço (ex: ABC1D23 ou ABC1234).");
        }

        return veiculoRepository.findByPlaca(placa)
                .orElseThrow(() -> new IllegalArgumentException("Veículo não encontrado!"));
    }

    public List<VeiculoModel> findByClienteCpf(String clienteCpf) {
        if (clienteCpf == null || !clienteCpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 números!");
        }

        return veiculoRepository.findByClienteCpf(clienteCpf);
    }

    public void save(VeiculoDto veiculoDto) {

        validar(veiculoDto);

        ClienteModel cliente = clienteRepository.findByCpf(veiculoDto.getClienteCpf())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        veiculoRepository.save(VeiculoModel.builder()
                .marca(veiculoDto.getMarca())
                .modelo(veiculoDto.getModelo())
                .ano(veiculoDto.getAno())
                .cor(veiculoDto.getCor())
                .placa(veiculoDto.getPlaca())
                .cliente(cliente)
                .tipoVeiculo(veiculoDto.getTipoVeiculo())
                .build());
    }

    public VeiculoModel update(String placa, VeiculoDto veiculoDto) {

        validar(veiculoDto);

        VeiculoModel veiculo = findByPlaca(placa);

        ClienteModel cliente = clienteRepository.findByCpf(veiculoDto.getClienteCpf())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        veiculo.setMarca(veiculoDto.getMarca());
        veiculo.setModelo(veiculoDto.getModelo());
        veiculo.setAno(veiculoDto.getAno());
        veiculo.setCor(veiculoDto.getCor());
        veiculo.setPlaca(veiculoDto.getPlaca());
        veiculo.setCliente(cliente);
        veiculo.setTipoVeiculo(veiculoDto.getTipoVeiculo());

        return veiculoRepository.save(veiculo);
    }

    private void validar(VeiculoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do veículo não informados!");
        }

        if (dto.getMarca() == null || dto.getMarca().isBlank()) {
            throw new IllegalArgumentException("Digite a marca!");
        }

        if (dto.getModelo() == null || dto.getModelo().isBlank()) {
            throw new IllegalArgumentException("Digite o modelo!");
        }

        if (dto.getAno() == null) {
            throw new IllegalArgumentException("Digite o ano!");
        }
        int anoAtual = java.time.Year.now().getValue();
        if (dto.getAno() < 1900 || dto.getAno() > anoAtual + 1) {
            throw new IllegalArgumentException("Ano inválido! Use um ano entre 1900 e " + (anoAtual + 1) + ".");
        }

        if (dto.getCor() == null || dto.getCor().isBlank()) {
            throw new IllegalArgumentException("Digite a cor!");
        }

        if (dto.getPlaca() == null || dto.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Digite a placa!");
        }
        if (!dto.getPlaca().matches("[A-Z]{3}\\d[A-Z0-9]\\d{2}")) {
            throw new IllegalArgumentException(
                    "Placa inválida! Use 3 letras maiúsculas, 1 número, 1 letra ou número e 2 números, sem traço (ex: ABC1D23 ou ABC1234).");
        }

        if (dto.getClienteCpf() == null || dto.getClienteCpf().isBlank()) {
            throw new IllegalArgumentException("Digite o CPF do cliente!");
        }
        if (!dto.getClienteCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 números, sem pontos ou traço!");
        }

        if (dto.getTipoVeiculo() == null) {
            throw new IllegalArgumentException("Informe o tipo do veículo!");
        }
    }

    public void delete(Integer id) {
        veiculoRepository.delete(findById(id));
    }

    public void deleteByPlaca(String placa) {
        VeiculoModel veiculo = findByPlaca(placa);

        veiculoRepository.delete(veiculo);
    }
}
