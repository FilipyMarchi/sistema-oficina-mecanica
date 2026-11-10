package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.FornecedorDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FornecedorModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public List<FornecedorModel> findAll() {
        return fornecedorRepository.findAll();
    }

    public FornecedorModel findById(Integer id) {
        return fornecedorRepository.findById(id).orElse(null);
    }

    public FornecedorModel findByNome(String nome) {
        return fornecedorRepository.findByNome(nome).orElse(null);
    }

    public void save(FornecedorDto fornecedorDto) {
        validar(fornecedorDto);

        // Verifica se já existe fornecedor com o mesmo nome ou telefone únicos
        if (fornecedorRepository.findByNome(fornecedorDto.getNome()).isPresent()) {
            throw new RuntimeException("Já existe um fornecedor cadastrado com este nome!");
        }

        fornecedorRepository.save(FornecedorModel.builder()
                .nome(fornecedorDto.getNome())
                .telefone(fornecedorDto.getTelefone())
                .tipo(fornecedorDto.getTipo())
                .build());
    }

    public FornecedorModel update(Integer id, FornecedorDto fornecedorDto) {
        validar(fornecedorDto);

        FornecedorModel fornecedor = findById(id);
        if (fornecedor == null) {
            throw new RuntimeException("Fornecedor não encontrado para atualização!");
        }

        fornecedor.setNome(fornecedorDto.getNome());
        fornecedor.setTelefone(fornecedorDto.getTelefone());
        fornecedor.setTipo(fornecedorDto.getTipo());

        return fornecedorRepository.save(fornecedor);
    }

    private void validar(FornecedorDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do fornecedor não informados!");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do fornecedor!");
        }

        if (dto.getTelefone() == null || dto.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Informe o telefone do fornecedor!");
        }
    }

    public void delete(Integer id) {
        FornecedorModel fornecedor = findById(id);
        if (fornecedor == null) {
            throw new RuntimeException("Fornecedor não encontrado para exclusão!");
        }
        fornecedorRepository.delete(fornecedor);
    }
}