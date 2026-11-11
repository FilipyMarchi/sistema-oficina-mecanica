package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.PromocaoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PromocaoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.PromocaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/promocao")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoService promocaoService;

    @GetMapping
    public List<PromocaoModel> findAll() {
        return promocaoService.findAll();
    }

    @GetMapping("/ativas")
    public List<PromocaoModel> findAtivas() {
        return promocaoService.findAtivas();
    }

    @GetMapping("/id/{id}")
    public PromocaoModel findById(@PathVariable Integer id) {
        return promocaoService.findById(id);
    }

    @PostMapping
    public HttpStatus save(@RequestBody PromocaoDto promocaoDto) {
        promocaoService.save(promocaoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public PromocaoModel update(@PathVariable Integer id, @RequestBody PromocaoDto promocaoDto) {
        return promocaoService.update(id, promocaoDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        promocaoService.delete(id);
        return HttpStatus.OK;
    }
}