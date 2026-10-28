package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.MecanicoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.MecanicoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.MecanicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/mecanico")
@RequiredArgsConstructor
public class MecanicoController {

    private final MecanicoService mecanicoService;

    @GetMapping
    public List<MecanicoModel> findAll (){
        return mecanicoService.findAll();
    }

    @GetMapping("/id/{id}")
    public MecanicoModel findById (@PathVariable Integer id){
        return mecanicoService.findById(id);
    }

    @GetMapping("nome/{nome}")
    public MecanicoModel findByCpf (@PathVariable String nome) {
        return mecanicoService.findByNome(nome);
    }

    @PostMapping
    public HttpStatus save (@RequestBody MecanicoDto mecanicoDto){
        mecanicoService.save(mecanicoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/cpf/{cpf}")
    public MecanicoModel update (@PathVariable String cpf, @RequestBody MecanicoDto mecanicoDto){
        return mecanicoService.update(cpf, mecanicoDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete (@PathVariable Integer id){
        mecanicoService.delete(id);
        return HttpStatus.OK;
    }

    @DeleteMapping("/nome/{nome}")
    public HttpStatus deleteByNome (@PathVariable String nome){
        mecanicoService.deleteByNome(nome);
        return HttpStatus.OK;
    }
}
