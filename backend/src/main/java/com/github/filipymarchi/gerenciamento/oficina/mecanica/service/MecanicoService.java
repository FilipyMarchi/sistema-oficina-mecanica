package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.MecanicoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.MecanicoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.MecanicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MecanicoService {

    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1621.00");

    private final MecanicoRepository mecanicoRepository;

    public List<MecanicoModel> findAll(){
        return mecanicoRepository.findAll();
    }

    public MecanicoModel findById(Integer id){
        return mecanicoRepository.findById(id).orElse(null);
    }

    public MecanicoModel findByCpf(String cpf) {
        return mecanicoRepository.findByCpf(cpf).orElse(null);
    }

    public void save (MecanicoDto mecanicoDto){
        validar(mecanicoDto);

        mecanicoRepository.save(MecanicoModel.builder()
                        .nome(mecanicoDto.getNome())
                        .telefone(mecanicoDto.getTelefone())
                        .cpf(mecanicoDto.getCpf())
                        .endereco(mecanicoDto.getEndereco())
                        .salario(mecanicoDto.getSalario())
                        .especialidade(mecanicoDto.getEspecialidade())
                .build());
    }

    public MecanicoModel update (String cpf, MecanicoDto mecanicoDto){
        validar(mecanicoDto);

        MecanicoModel mecanico = findByCpf(cpf);

        mecanico.setNome(mecanicoDto.getNome());
        mecanico.setTelefone(mecanicoDto.getTelefone());
        mecanico.setCpf(mecanicoDto.getCpf());
        mecanico.setEndereco(mecanicoDto.getEndereco());
        mecanico.setSalario(mecanicoDto.getSalario());
        mecanico.setEspecialidade(mecanicoDto.getEspecialidade());

        return mecanicoRepository.save(mecanico);
    }

    private void validar(MecanicoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do mecânico não informados!");
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Digite um nome!");
        }

        if (dto.getTelefone() == null || dto.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Digite um telefone!");
        }
        if (!dto.getTelefone().replaceAll("\\D", "").matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone inválido! Use DDD + número.");
        }

        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new IllegalArgumentException("Digite um CPF!");
        }
        if (!dto.getTelefone().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone inválido! Use DDD + número (Apenas números).");
        }

        if (dto.getEndereco() == null || dto.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Digite um endereço!");
        }

        if (dto.getSalario() == null) {
            throw new IllegalArgumentException("Digite o salário!");
        }
        if (dto.getSalario().compareTo(SALARIO_MINIMO) < 0) {
            throw new IllegalArgumentException("Salário não pode ser menor que o salário mínimo (R$ 1.621,00)!");
        }
        if (dto.getSalario().scale() > 2) {
            throw new IllegalArgumentException("Salário deve ter no máximo 2 casas decimais!");
        }
        if (dto.getSalario().precision() - dto.getSalario().scale() > 8) {
            throw new IllegalArgumentException("Salário muito alto!");
        }

        if (dto.getEspecialidade() == null) {
            throw new IllegalArgumentException("Informe a especialidade!");
        }
    }

    public void delete (Integer id){
        mecanicoRepository.delete(findById(id));
    }

    public void deleteByCpf (String cpf){
        mecanicoRepository.delete(findByCpf(cpf));
    }
}
