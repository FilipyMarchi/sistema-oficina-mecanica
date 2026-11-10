package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.PecaDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FornecedorModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PecaModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.FornecedorRepository;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PecaService {

    private final PecaRepository pecaRepository;
    private final FornecedorRepository fornecedorRepository;

    public List<PecaModel> findAll() {
        return pecaRepository.findAll();
    }

    public PecaModel findById(Integer id) {
        return pecaRepository.findById(id).orElse(null);
    }

    public PecaModel findByNome(String nome) {
        return pecaRepository.findByNome(nome).orElse(null);
    }

    public List<PecaModel> findByFornecedorNome(String fornecedorNome) {
        return pecaRepository.findByFornecedorNome(fornecedorNome);
    }

    public void save(PecaDto pecaDto) {
        validar(pecaDto);

        FornecedorModel fornecedor = fornecedorRepository.findByNome(pecaDto.getFornecedorNome())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        pecaRepository.save(PecaModel.builder()
                .nome(pecaDto.getNome())
                .valor(pecaDto.getValor())
                .quantidade(pecaDto.getQuantidade())
                .fornecedor(fornecedor)
                .garantiaValidade(pecaDto.getGarantiaValidade())
                .build());
    }

    public PecaModel update(Integer id, PecaDto pecaDto) {
        validar(pecaDto);

        PecaModel peca = findById(id);
        if (peca == null) {
            throw new RuntimeException("Peça não encontrada para atualização!");
        }

        FornecedorModel fornecedor = fornecedorRepository.findByNome(pecaDto.getFornecedorNome())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        peca.setNome(pecaDto.getNome());
        peca.setValor(pecaDto.getValor());
        peca.setQuantidade(pecaDto.getQuantidade());
        peca.setFornecedor(fornecedor);
        peca.setGarantiaValidade(pecaDto.getGarantiaValidade());

        return pecaRepository.save(peca);
    }

    private void validar(PecaDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da peça não informados!");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome da peça!");
        }

        if (dto.getValor() < 0) {
            throw new IllegalArgumentException("O valor da peça não pode ser negativo!");
        }

        if (dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa!");
        }

        if (dto.getFornecedorNome() == null || dto.getFornecedorNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do fornecedor!");
        }

        if (dto.getGarantiaValidade() == null) {
            throw new IllegalArgumentException("Informe a validade da garantia!");
        }
    }

    public void delete(Integer id) {
        PecaModel peca = findById(id);
        if (peca == null) {
            throw new RuntimeException("Peça não encontrada para exclusão!");
        }
        pecaRepository.delete(peca);
    }
}