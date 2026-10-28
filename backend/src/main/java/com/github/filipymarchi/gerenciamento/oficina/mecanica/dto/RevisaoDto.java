package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.StatusRevisao;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class RevisaoDto {
    @NonNull
    private String veiculoPlaca;

    @NonNull
    private String mecanicoNome;

    @NonNull
    private Integer kmAtual;

    @NonNull
    private Integer proxRevisaoKm;

    @NonNull
    private LocalDate dataHora;

    @NonNull
    private StatusRevisao status;
}
