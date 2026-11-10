package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class PecaDto {

    @NonNull
    private String nome;

    @NonNull
    private double valor;

    @NonNull
    private int quantidade;

    @NonNull
    private String fornecedorNome;

    @NonNull
    private LocalDate garantiaValidade;
}