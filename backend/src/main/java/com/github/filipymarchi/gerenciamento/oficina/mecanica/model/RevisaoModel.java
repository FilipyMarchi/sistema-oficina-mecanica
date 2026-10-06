package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "revisao")
public class RevisaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoModel veiculo;

    @Column(nullable = false)
    private int kmAtual;

    @Column(nullable = false)
    private int proxRevisaoKm;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mecanico_id", nullable = false)
    private MecanicoModel mecanico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_id", nullable = false)
    private BoxAtendimentoModel box;
}
