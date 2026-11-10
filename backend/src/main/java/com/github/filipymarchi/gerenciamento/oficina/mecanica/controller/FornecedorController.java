package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.FornecedorDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FornecedorModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/fornecedor")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping
    public List<FornecedorModel> findAll() {
        return fornecedorService.findAll();
    }

    @GetMapping("/id/{id}")
    public FornecedorModel findById(@PathVariable Integer id) {
        return fornecedorService.findById(id);
    }

    @GetMapping("/nome/{nome}")
    public FornecedorModel findByNome(@PathVariable String nome) {
        return fornecedorService.findByNome(nome);
    }

    @PostMapping
    public HttpStatus save(@RequestBody FornecedorDto fornecedorDto) {
        fornecedorService.save(fornecedorDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public FornecedorModel update(@PathVariable Integer id, @RequestBody FornecedorDto fornecedorDto) {
        return fornecedorService.update(id, fornecedorDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        fornecedorService.delete(id);
        return HttpStatus.OK;
    }
}