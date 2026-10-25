package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.ClienteDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ClienteModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody ClienteModel request) {
        boolean autenticado = clienteService.autenticar(request.getCpf(), request.getSenha());

        return autenticado
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public List<ClienteModel> findAll(){
        return clienteService.findAll();
    }

    @GetMapping("/id/{id}")
    public ClienteModel findById(@PathVariable Integer id){
        return clienteService.findById(id);
    }

    @GetMapping("/cpf/{cpf}")
    public ClienteModel findByCpf(@PathVariable String cpf){
        return clienteService.findByCpf(cpf);
    }

    @GetMapping("/email/{email}")
    public ClienteModel findByEmail(@PathVariable String email){
        return clienteService.findByEmail(email);
    }

    @PostMapping
    public HttpStatus save(@Validated @RequestBody ClienteDto clienteDto){
        clienteService.save(clienteDto);
        return HttpStatus.OK;
    }

    @PutMapping("/{cpf}")
    public ClienteModel update (@PathVariable String cpf, @RequestBody ClienteDto clienteDto){
        return clienteService.update(cpf, clienteDto);
    }

    @DeleteMapping("/id/{id}")
    public HttpStatus delete (@PathVariable Integer id){
        clienteService.delete(id);
        return HttpStatus.OK;
    }

    @DeleteMapping("/cpf/{cpf}")
    public HttpStatus deleteByCpf (@PathVariable String cpf){
        clienteService.deleteByCpf(cpf);
        return HttpStatus.OK;
    }
}
