package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.ClienteDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ClienteModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public boolean autenticar(String cpf, String senha) {
        return clienteRepository.findByCpf(cpf)
                .map(cliente -> cliente.getSenha().equals(senha))
                .orElse(false);
    }

    public List<ClienteModel> findAll() {
        return clienteRepository.findAll();
    }

    public ClienteModel findByEmail(String email){
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Digite um E-mail!");
        }

        if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("E-mail inválido!");
        }

        return clienteRepository.findByEmail(email).orElse(null);
    }

    public ClienteModel findByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Digite um CPF!");
        }

        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 números, sem pontos ou traço!");
        }

        return clienteRepository.findByCpf(cpf).orElse(null);
    }

    public ClienteModel findById(Integer id){
        return clienteRepository.findById(id).orElse(null);
    }

    public void save (ClienteDto clienteDto){

        validar(clienteDto);

        clienteRepository.save(ClienteModel.builder()
                        .nome(clienteDto.getNome())
                        .telefone(clienteDto.getTelefone())
                        .cpf(clienteDto.getCpf())
                        .endereco(clienteDto.getEndereco())
                        .email(clienteDto.getEmail())
                        .senha(clienteDto.getSenha())
                .build());
    }

    public ClienteModel update (String cpf, ClienteDto clienteDto){
        validar(clienteDto);

        ClienteModel cliente = findByCpf(cpf);

        cliente.setNome(clienteDto.getNome());
        cliente.setTelefone(clienteDto.getTelefone());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setEndereco(clienteDto.getEndereco());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setSenha(clienteDto.getSenha());

        return clienteRepository.save(cliente);
    }

    private void validar(ClienteDto dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Digite um nome!");
        }

        if (dto.getTelefone() == null || dto.getTelefone().isBlank()) {
            throw new IllegalArgumentException("Digite um telefone!");
        }
        if (!dto.getTelefone().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Telefone inválido! Use DDD + número (Apenas números).");
        }

        if (dto.getCpf() == null || dto.getCpf().isBlank()) {
            throw new IllegalArgumentException("Digite um CPF!");
        }
        if (!dto.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 números, sem pontos ou traço!");
        }

        if (dto.getEndereco() == null || dto.getEndereco().isBlank()) {
            throw new IllegalArgumentException("Digite um endereço!");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Digite um e-mail!");
        }
        if (!dto.getEmail().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            throw new IllegalArgumentException("E-mail inválido!");
        }

        if (dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new IllegalArgumentException("Digite uma senha!");
        }
        if (dto.getSenha().length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres!");
        }
    }

    public void delete (Integer id){
        clienteRepository.delete(findById(id));
    }

    public void deleteByCpf (String cpf){
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Digite um CPF!");
        }
        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 números, sem pontos ou traço!");
        }

        ClienteModel cliente = clienteRepository.findByCpf(cpf).orElse(null);
        clienteRepository.delete(cliente);
    }
}
