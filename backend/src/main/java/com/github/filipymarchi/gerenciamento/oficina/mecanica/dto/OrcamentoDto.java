package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoDto {

    @NonNull
    private Integer revisaoId;

    @Builder.Default
    private List<Integer> servicosIds = new ArrayList<>();

    @Builder.Default
    private List<Integer> pecasIds = new ArrayList<>();

    @Builder.Default
    private List<Integer> fluidosIds = new ArrayList<>();
}