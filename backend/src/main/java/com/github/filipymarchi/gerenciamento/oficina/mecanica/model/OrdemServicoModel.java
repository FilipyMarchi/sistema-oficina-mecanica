package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.StatusOS;
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
@Table(name = "ordem_servico")
public class OrdemServicoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revisao_id", nullable = false)
    private RevisaoModel revisao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private OrcamentoModel orcamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private PagamentoModel pagamento;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusOS status;

    @Column(nullable = false)
    private LocalDate dataHora;
}
