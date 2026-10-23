package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.MecanicoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MecanicoRepository extends JpaRepository<MecanicoModel, Integer> {
    Optional<MecanicoModel> findByCpf(String cpf);
}
