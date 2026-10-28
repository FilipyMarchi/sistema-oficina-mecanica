package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.StatusRevisao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mecanico_id", nullable = false)
    private MecanicoModel mecanico;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "orcamento_id", nullable = false)
//    private OrcamentoModel orcamento;

    @Column(nullable = false)
    private Integer kmAtual;

    @Column(nullable = false)
    private Integer proxRevisaoKm;

    @Column(nullable = false)
    private LocalDate dataHora;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusRevisao status;
}
