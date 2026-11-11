package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PromocaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromocaoRepository extends JpaRepository<PromocaoModel, Integer> {

    List<PromocaoModel> findByDataExpiracaoGreaterThanEqual(LocalDate dataAtual);
}