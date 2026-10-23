package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.TipoFornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "fornecedor")
public class FornecedorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column
    private TipoFornecedor tipo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    private List<FluidoModel> fluidos = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
    private List<PecaModel> pecas = new ArrayList<>();
}
