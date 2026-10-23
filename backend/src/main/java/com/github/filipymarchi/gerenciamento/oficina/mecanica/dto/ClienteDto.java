package com.github.filipymarchi.gerenciamento.oficina.mecanica.dto;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.VeiculoModel;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClienteDto {
    @NonNull
    private String nome;
    @NonNull
    private String telefone;
    @NonNull
    private String cpf;
    @NonNull
    private String endereco;
    @NonNull
    private String email;
    @NonNull
    private String senha;
}
