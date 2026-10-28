package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "mecanico")
public class MecanicoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salario;

    @Column(nullable = false)
    private String especialidade;
}
