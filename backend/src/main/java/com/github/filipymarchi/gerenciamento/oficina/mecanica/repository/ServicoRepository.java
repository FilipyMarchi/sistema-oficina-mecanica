package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoModel, Integer> {

    Optional<ServicoModel> findByTipoServico(String tipoServico);
}
