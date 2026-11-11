package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FluidoDto {

    @NonNull
    private String marca;

    @NonNull
    private double valor;

    @NonNull
    private String tipo;

    @NonNull
    private LocalDate validade;

    @NonNull
    private int quantidade;

    @NonNull
    private String fornecedorNome;
}