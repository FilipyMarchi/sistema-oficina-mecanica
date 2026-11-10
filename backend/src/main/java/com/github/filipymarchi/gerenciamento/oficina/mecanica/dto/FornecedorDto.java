package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.TipoFornecedor;
import lombok.Data;
import lombok.NonNull;

@Data
public class FornecedorDto {

    @NonNull
    private String nome;

    @NonNull
    private String telefone;

    private TipoFornecedor tipo;
}