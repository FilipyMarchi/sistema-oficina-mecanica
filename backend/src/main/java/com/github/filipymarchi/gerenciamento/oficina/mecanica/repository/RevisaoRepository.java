package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.RevisaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevisaoRepository extends JpaRepository<RevisaoModel, Integer> {
}
