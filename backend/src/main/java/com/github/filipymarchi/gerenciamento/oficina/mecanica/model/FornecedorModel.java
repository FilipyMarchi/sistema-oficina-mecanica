package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

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

    @OneToMany(mappedBy = "fornecedor")
    private List<FluidoModel> fluidos = new ArrayList<>();

    @OneToMany(mappedBy = "fornecedor")
    private List<PecaModel> pecas = new ArrayList<>();
}
