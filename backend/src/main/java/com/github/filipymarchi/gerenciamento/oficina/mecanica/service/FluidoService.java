package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.FluidoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FluidoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FornecedorModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.FluidoRepository;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FluidoService {

    private final FluidoRepository fluidoRepository;
    private final FornecedorRepository fornecedorRepository;

    public List<FluidoModel> findAll() {
        return fluidoRepository.findAll();
    }

    public FluidoModel findById(Integer id) {
        return fluidoRepository.findById(id).orElse(null);
    }

    public List<FluidoModel> findByFornecedorNome(String fornecedorNome) {
        return fluidoRepository.findByFornecedorNome(fornecedorNome);
    }

    public void save(FluidoDto fluidoDto) {
        validar(fluidoDto);

        FornecedorModel fornecedor = fornecedorRepository.findByNome(fluidoDto.getFornecedorNome())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fluidoRepository.save(FluidoModel.builder()
                .marca(fluidoDto.getMarca())
                .valor(fluidoDto.getValor())
                .tipo(fluidoDto.getTipo())
                .validade(fluidoDto.getValidade())
                .quantidade(fluidoDto.getQuantidade())
                .fornecedor(fornecedor)
                .build());
    }

    public FluidoModel update(Integer id, FluidoDto fluidoDto) {
        validar(fluidoDto);

        FluidoModel fluido = findById(id);
        if (fluido == null) {
            throw new RuntimeException("Fluido não encontrado para atualização!");
        }

        FornecedorModel fornecedor = fornecedorRepository.findByNome(fluidoDto.getFornecedorNome())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        fluido.setMarca(fluidoDto.getMarca());
        fluido.setValor(fluidoDto.getValor());
        fluido.setTipo(fluidoDto.getTipo());
        fluido.setValidade(fluidoDto.getValidade());
        fluido.setQuantidade(fluidoDto.getQuantidade());
        fluido.setFornecedor(fornecedor);

        return fluidoRepository.save(fluido);
    }

    private void validar(FluidoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do fluido não informados!");
        }

        if (dto.getMarca() == null || dto.getMarca().isBlank()) {
            throw new IllegalArgumentException("Informe a marca do fluido!");
        }

        if (dto.getValor() < 0) {
            throw new IllegalArgumentException("O valor do fluido não pode ser negativo!");
        }

        if (dto.getTipo() == null || dto.getTipo().isBlank()) {
            throw new IllegalArgumentException("Informe o tipo do fluido!");
        }

        if (dto.getValidade() == null) {
            throw new IllegalArgumentException("Informe a data de validade do fluido!");
        }

        if (dto.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa!");
        }

        if (dto.getFornecedorNome() == null || dto.getFornecedorNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do fornecedor!");
        }
    }

    public void delete(Integer id) {
        FluidoModel fluido = findById(id);
        if (fluido == null) {
            throw new RuntimeException("Fluido não encontrado para exclusão!");
        }
        fluidoRepository.delete(fluido);
    }
}