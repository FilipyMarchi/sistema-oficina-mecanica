package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.OrcamentoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.OrcamentoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.OrcamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/orcamento")
@RequiredArgsConstructor
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    @GetMapping
    public List<OrcamentoModel> findAll() {
        return orcamentoService.findAll();
    }

    @GetMapping("/id/{id}")
    public OrcamentoModel findById(@PathVariable Integer id) {
        return orcamentoService.findById(id);
    }

    @GetMapping("/revisao/{revisaoId}")
    public OrcamentoModel findByRevisaoId(@PathVariable Integer revisaoId) {
        return orcamentoService.findByRevisaoId(revisaoId);
    }

    @PostMapping
    public HttpStatus save(@RequestBody OrcamentoDto dto) {
        orcamentoService.save(dto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/{id}")
    public OrcamentoModel update(@PathVariable Integer id, @RequestBody OrcamentoDto dto) {
        return orcamentoService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        orcamentoService.delete(id);
        return HttpStatus.OK;
    }
}