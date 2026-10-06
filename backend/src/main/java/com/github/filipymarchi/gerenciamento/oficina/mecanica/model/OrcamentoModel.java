package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "orcamento")
public class OrcamentoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double valorTotal;

    @ManyToMany
    @JoinTable (name = "orcamento_servico",
            joinColumns = @JoinColumn(name = "orcamento_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id"))
    private List<ServicoModel> servicos = new ArrayList<>();

    @ManyToMany
    @JoinTable (name = "orcamento_peca",
            joinColumns = @JoinColumn(name = "orcamento_id"),
            inverseJoinColumns = @JoinColumn(name = "peca_id"))
    private List<PecaModel> pecasUtilizadas = new ArrayList<>();

    @ManyToMany
    @JoinTable (name = "orcamento_fluido",
            joinColumns = @JoinColumn(name = "orcamento_id"),
            inverseJoinColumns = @JoinColumn(name = "fluido_id"))
    private List<FluidoModel> fluidosUtilizados = new ArrayList<>();
}
