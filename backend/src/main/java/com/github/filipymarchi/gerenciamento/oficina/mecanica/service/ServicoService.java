package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.ServicoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ServicoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public List<ServicoModel> findAll() {
        return servicoRepository.findAll();
    }

    public ServicoModel findById(Integer id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public ServicoModel findByTipoServico(String tipoServico) {
        return servicoRepository.findByTipoServico(tipoServico).orElse(null);
    }

    public void save(ServicoDto servicoDto) {
        validar(servicoDto);

        servicoRepository.save(ServicoModel.builder()
                .tipoServico(servicoDto.getTipoServico())
                .valor(servicoDto.getValor())
                .garantiaValidade(servicoDto.getGarantiaValidade())
                .build());
    }

    public ServicoModel update(Integer id, ServicoDto servicoDto) {
        validar(servicoDto);

        ServicoModel servico = findById(id);
        if (servico == null) {
            throw new RuntimeException("Serviço não encontrado para atualização!");
        }

        servico.setTipoServico(servicoDto.getTipoServico());
        servico.setValor(servicoDto.getValor());
        servico.setGarantiaValidade(servicoDto.getGarantiaValidade());

        return servicoRepository.save(servico);
    }

    private void validar(ServicoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do serviço não informados!");
        }

        if (dto.getTipoServico() == null || dto.getTipoServico().isBlank()) {
            throw new IllegalArgumentException("Informe o tipo de serviço!");
        }

        if (dto.getValor() < 0) {
            throw new IllegalArgumentException("O valor do serviço não pode ser negativo!");
        }

        if (dto.getGarantiaValidade() == null) {
            throw new IllegalArgumentException("Informe a validade da garantia!");
        }
    }

    public void delete(Integer id) {
        ServicoModel servico = findById(id);
        if (servico == null) {
            throw new RuntimeException("Serviço não encontrado para exclusão!");
        }
        servicoRepository.delete(servico);
    }
}