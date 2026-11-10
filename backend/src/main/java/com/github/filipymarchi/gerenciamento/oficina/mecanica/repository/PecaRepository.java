package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PecaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PecaRepository extends JpaRepository<PecaModel, Integer> {

    Optional<PecaModel> findByNome(String nome);

    List<PecaModel> findByFornecedorNome(String fornecedorNome);
}