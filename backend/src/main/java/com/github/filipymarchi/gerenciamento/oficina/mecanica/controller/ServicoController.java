package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.ServicoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ServicoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/servico")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @GetMapping
    public List<ServicoModel> findAll() {
        return servicoService.findAll();
    }

    @GetMapping("/id/{id}")
    public ServicoModel findById(@PathVariable Integer id) {
        return servicoService.findById(id);
    }

    @GetMapping("/tipo/{tipoServico}")
    public ServicoModel findByTipoServico(@PathVariable String tipoServico) {
        return servicoService.findByTipoServico(tipoServico);
    }

    @PostMapping
    public HttpStatus save(@RequestBody ServicoDto servicoDto) {
        servicoService.save(servicoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public ServicoModel update(@PathVariable Integer id, @RequestBody ServicoDto servicoDto) {
        return servicoService.update(id, servicoDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        servicoService.delete(id);
        return HttpStatus.OK;
    }
}