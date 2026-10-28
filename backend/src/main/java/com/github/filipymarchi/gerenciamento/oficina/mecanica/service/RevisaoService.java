package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.RevisaoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.MecanicoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.RevisaoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.VeiculoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.MecanicoRepository;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.RevisaoRepository;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RevisaoService {
    private final RevisaoRepository revisaoRepository;
    private final MecanicoRepository mecanicoRepository;
    private final VeiculoRepository veiculoRepository;

    private static final String REGEX_PLACA = "[A-Z]{3}\\d[A-Z0-9]\\d{2}";

    public List<RevisaoModel> findAll (){
        return revisaoRepository.findAll();
    }

    public RevisaoModel findById (Integer id){
        return revisaoRepository.findById(id).orElse(null);
    }

    public void save (RevisaoDto revisaoDto){
        validar(revisaoDto);

        MecanicoModel mecanico = mecanicoRepository.findByNome(revisaoDto.getMecanicoNome())
                .orElseThrow(() -> new RuntimeException("Mecanico não encontrado"));

        VeiculoModel veiculo = veiculoRepository.findByPlaca(revisaoDto.getVeiculoPlaca())
                .orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));

        revisaoRepository.save(RevisaoModel.builder()
                        .veiculo(veiculo)
                        .mecanico(mecanico)
                        .kmAtual(revisaoDto.getKmAtual())
                        .proxRevisaoKm(revisaoDto.getProxRevisaoKm())
                        .dataHora(revisaoDto.getDataHora())
                        .status(revisaoDto.getStatus())
                .build());
    }

    public RevisaoModel update (Integer id, RevisaoDto revisaoDto){
        validar(revisaoDto);

        RevisaoModel revisao = findById(id);

        MecanicoModel mecanico = mecanicoRepository.findByNome(revisaoDto.getMecanicoNome())
                .orElseThrow(() -> new RuntimeException("Mecanico não encontrado"));

        revisao.setMecanico(mecanico);
        revisao.setKmAtual(revisaoDto.getKmAtual());
        revisao.setProxRevisaoKm(revisaoDto.getProxRevisaoKm());
        revisao.setDataHora(revisaoDto.getDataHora());
        revisao.setStatus(revisaoDto.getStatus());

        return revisaoRepository.save(revisao);
    }

    private void validar(RevisaoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da revisão não informados!");
        }

        if (dto.getMecanicoNome() == null || dto.getMecanicoNome().isBlank()) {
            throw new IllegalArgumentException("Digite o nome do mecânico!");
        }

        if (dto.getVeiculoPlaca() == null || dto.getVeiculoPlaca().isBlank()) {
            throw new IllegalArgumentException("Digite a placa do veículo!");
        }
        if (!dto.getVeiculoPlaca().matches(REGEX_PLACA)) {
            throw new IllegalArgumentException(
                    "Placa inválida! Use 3 letras maiúsculas, 1 número, 1 letra ou número e 2 números, sem traço (ex: ABC1D23 ou ABC1234).");
        }

        if (dto.getKmAtual() == null) {
            throw new IllegalArgumentException("Digite a quilometragem atual!");
        }
        if (dto.getKmAtual() < 0) {
            throw new IllegalArgumentException("A quilometragem atual não pode ser negativa!");
        }

        if (dto.getProxRevisaoKm() == null) {
            throw new IllegalArgumentException("Digite a quilometragem da próxima revisão!");
        }
        if (dto.getProxRevisaoKm() <= dto.getKmAtual()) {
            throw new IllegalArgumentException("A próxima revisão deve ser em uma quilometragem maior que a atual!");
        }
        if (dto.getDataHora() == null) {
            throw new IllegalArgumentException("Informe a data e hora da revisão!");
        }
        if (dto.getStatus() == null) {
            throw new IllegalArgumentException("Informe o status da revisão!");
        }
    }

    public void delete (Integer id){
        revisaoRepository.delete(findById(id));
    }
}
