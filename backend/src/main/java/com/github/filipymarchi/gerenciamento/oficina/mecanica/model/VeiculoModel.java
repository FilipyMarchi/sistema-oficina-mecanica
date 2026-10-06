package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.TipoVeiculo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "veiculo")
public class VeiculoModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private String cor;

    @Column(nullable = false, unique = true)
    private String placa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteModel cliente;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoVeiculo tipoVeiculo;
}
