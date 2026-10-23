package com.github.filipymarchi.gerenciamento.oficina.mecanica.repository;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.VeiculoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<VeiculoModel, Integer> {

    Optional<VeiculoModel> findByPlaca(String placa);

    List<VeiculoModel> findByClienteCpf(String clienteCpf);
}
