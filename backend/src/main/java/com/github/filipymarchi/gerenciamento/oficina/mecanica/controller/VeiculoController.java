package com.github.filipymarchi.gerenciamento.oficina.mecanica.controller;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.VeiculoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.VeiculoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.service.VeiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/oficina/veiculo")
@RequiredArgsConstructor
public class VeiculoController {
    private final VeiculoService veiculoService;

    @GetMapping
    public List<VeiculoModel> findAll() {
        return veiculoService.findAll();
    }

    @GetMapping("/id/{id}")
    public VeiculoModel findById(@PathVariable Integer id) {
        return veiculoService.findById(id);
    }

    @GetMapping("/placa/{placa}")
    public VeiculoModel findByPlaca(@PathVariable String placa) {
        return veiculoService.findByPlaca(placa);
    }

    @GetMapping("/clienteCpf/{clienteCpf}")
    public List<VeiculoModel> findByClienteId(@PathVariable String clienteCpf) {
        return veiculoService.findByClienteCpf(clienteCpf);
    }

    @PostMapping
    public HttpStatus save(@RequestBody VeiculoDto veiculoDto) {
        veiculoService.save(veiculoDto);
        return HttpStatus.CREATED;
    }

    @PutMapping("/placa/{placa}")
    public VeiculoModel updateByPlaca(@PathVariable String placa, @RequestBody VeiculoDto veiculoDto) {
        return veiculoService.updateByPlaca(placa, veiculoDto);
    }

    @PutMapping("/id/{id}")
    public VeiculoModel updateById(@PathVariable Integer id, @RequestBody VeiculoDto veiculoDto) {
        return veiculoService.updateById(id, veiculoDto);
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable Integer id) {
        veiculoService.delete(id);
        return HttpStatus.OK;
    }

    @DeleteMapping("/placa/{placa}")
    public HttpStatus deleteByPlaca(@PathVariable String placa) {
        veiculoService.deleteByPlaca(placa);
        return HttpStatus.OK;
    }
}
