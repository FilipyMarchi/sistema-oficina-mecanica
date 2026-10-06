package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass

public abstract class Pessoa {
    private Integer id;
    private String nome;
    private String telefone;
    private String cpf;
    private String endereco;
}
