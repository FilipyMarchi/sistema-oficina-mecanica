package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class ServicoDto {

    @NonNull
    private String tipoServico;

    @NonNull
    private double valor;

    @NonNull
    private LocalDate garantiaValidade;
}
