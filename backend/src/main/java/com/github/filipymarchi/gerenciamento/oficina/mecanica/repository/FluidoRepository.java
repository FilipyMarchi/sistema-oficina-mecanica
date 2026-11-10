package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FluidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FluidoRepository extends JpaRepository<FluidoModel, Integer> {

    Optional<FluidoModel> findByMarcaAndTipo(String marca, String tipo);

    List<FluidoModel> findByFornecedorNome(String fornecedorNome);
}