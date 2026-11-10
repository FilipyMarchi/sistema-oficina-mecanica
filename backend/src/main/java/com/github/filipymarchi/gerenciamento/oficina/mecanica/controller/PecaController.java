package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.PecaDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PecaModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.PecaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/peca")
@RequiredArgsConstructor
public class PecaController {

    private final PecaService pecaService;

    @GetMapping
    public List<PecaModel> findAll() {
        return pecaService.findAll();
    }

    @GetMapping("/id/{id}")
    public PecaModel findById(@PathVariable Integer id) {
        return pecaService.findById(id);
    }

    @GetMapping("/nome/{nome}")
    public PecaModel findByNome(@PathVariable String nome) {
        return pecaService.findByNome(nome);
    }

    @GetMapping("/fornecedor/{fornecedorNome}")
    public List<PecaModel> findByFornecedorNome(@PathVariable String fornecedorNome) {
        return pecaService.findByFornecedorNome(fornecedorNome);
    }

    @PostMapping
    public HttpStatus save(@RequestBody PecaDto pecaDto) {
        pecaService.save(pecaDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public PecaModel update(@PathVariable Integer id, @RequestBody PecaDto pecaDto) {
        return pecaService.update(id, pecaDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        pecaService.delete(id);
        return HttpStatus.OK;
    }
}