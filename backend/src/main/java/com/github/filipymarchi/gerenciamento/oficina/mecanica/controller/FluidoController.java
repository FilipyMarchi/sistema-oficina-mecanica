package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.FluidoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.FluidoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.FluidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/fluido")
@RequiredArgsConstructor
public class FluidoController {

    private final FluidoService fluidoService;

    @GetMapping
    public List<FluidoModel> findAll() {
        return fluidoService.findAll();
    }

    @GetMapping("/id/{id}")
    public FluidoModel findById(@PathVariable Integer id) {
        return fluidoService.findById(id);
    }

    @GetMapping("/fornecedor/{fornecedorNome}")
    public List<FluidoModel> findByFornecedorNome(@PathVariable String fornecedorNome) {
        return fluidoService.findByFornecedorNome(fornecedorNome);
    }

    @PostMapping
    public HttpStatus save(@RequestBody FluidoDto fluidoDto) {
        fluidoService.save(fluidoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public FluidoModel update(@PathVariable Integer id, @RequestBody FluidoDto fluidoDto) {
        return fluidoService.update(id, fluidoDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        fluidoService.delete(id);
        return HttpStatus.OK;
    }
}