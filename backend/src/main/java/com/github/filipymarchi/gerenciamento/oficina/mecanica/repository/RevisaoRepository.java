package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.RevisaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevisaoRepository extends JpaRepository<RevisaoModel, Integer> {

    Optional<RevisaoModel> findByVeiculoPlaca (String veiculoPlaca);
}
