package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class MecanicoDto {
    @NonNull
    private String nome;

    @NonNull
    private String telefone;

    @NonNull
    private String cpf;

    @NonNull
    private String endereco;

    @NonNull
    private BigDecimal salario;

    @NonNull
    private String especialidade;
}
