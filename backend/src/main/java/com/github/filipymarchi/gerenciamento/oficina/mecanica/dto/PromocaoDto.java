package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class PromocaoDto {

    @NonNull
    private String mensagem;

    @NonNull
    private LocalDate dataExpiracao;
}