package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.RevisaoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.RevisaoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.RevisaoService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oficina/revisao")
@RequiredArgsConstructor
public class RevisaoController {
    private final RevisaoService revisaoService;

    @GetMapping
    public List<RevisaoModel> findAll (){
        return revisaoService.findAll();
    }

    @GetMapping("/id/{id}")
    public RevisaoModel findById (@PathVariable Integer id){
        return revisaoService.findById(id);
    }

    @PostMapping
    public HttpStatus save (@RequestBody RevisaoDto revisaoDto){
        revisaoService.save(revisaoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/id/{id}")
    public RevisaoModel update (@PathVariable Integer id, @RequestBody RevisaoDto revisaoDto){
        return revisaoService.update(id, revisaoDto);
    }

    @DeleteMapping("/id/{id}")
    public HttpStatus delete (@PathVariable Integer id){
        revisaoService.delete(id);
        return HttpStatus.OK;
    }
}
