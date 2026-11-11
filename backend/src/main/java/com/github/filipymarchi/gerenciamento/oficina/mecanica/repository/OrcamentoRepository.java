package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.OrcamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrcamentoRepository extends JpaRepository<OrcamentoModel, Integer> {

    Optional<OrcamentoModel> findByRevisaoId(Integer revisaoId);
}