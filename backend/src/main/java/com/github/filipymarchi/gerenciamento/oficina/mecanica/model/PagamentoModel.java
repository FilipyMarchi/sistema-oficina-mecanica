package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.FormaPagamento;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.StatusPagamento;
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
@Table(name = "pagamento")
public class PagamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double valorPago;

    @Column(nullable = false)
    private LocalDate dataPagamento;

    @Column(nullable = false)
    private StatusPagamento statusPagamento;

    @Enumerated(EnumType.STRING)
    @Column
    private FormaPagamento formaPagamento;
}
