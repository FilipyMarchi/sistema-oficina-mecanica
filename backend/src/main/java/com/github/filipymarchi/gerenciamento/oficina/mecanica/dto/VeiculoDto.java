package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.TipoVeiculo;
import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class VeiculoDto {

    @NonNull
    private String marca;
    @NonNull
    private String modelo;
    @NonNull
    private Integer ano;
    @NonNull
    private String cor;
    @NonNull
    private String placa;
    @NonNull
    private String clienteCpf;
    @NonNull
    private TipoVeiculo tipoVeiculo;
}
