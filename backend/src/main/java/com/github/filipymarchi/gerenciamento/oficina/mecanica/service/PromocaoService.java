package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.PromocaoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.PromocaoModel;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.PromocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;

    public List<PromocaoModel> findAll() {
        return promocaoRepository.findAll();
    }

    public List<PromocaoModel> findAtivas() {
        return promocaoRepository.findByDataExpiracaoGreaterThanEqual(LocalDate.now());
    }

    public PromocaoModel findById(Integer id) {
        return promocaoRepository.findById(id).orElse(null);
    }

    public void save(PromocaoDto promocaoDto) {
        validar(promocaoDto);

        promocaoRepository.save(PromocaoModel.builder()
                .mensagem(promocaoDto.getMensagem())
                .dataExpiracao(promocaoDto.getDataExpiracao())
                .build());
    }

    public PromocaoModel update(Integer id, PromocaoDto promocaoDto) {
        validar(promocaoDto);

        PromocaoModel promocao = findById(id);
        if (promocao == null) {
            throw new RuntimeException("Promoção não encontrada para atualização!");
        }

        promocao.setMensagem(promocaoDto.getMensagem());
        promocao.setDataExpiracao(promocaoDto.getDataExpiracao());

        return promocaoRepository.save(promocao);
    }

    private void validar(PromocaoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da promoção não informados!");
        }

        if (dto.getMensagem() == null || dto.getMensagem().isBlank()) {
            throw new IllegalArgumentException("Informe a mensagem da promoção!");
        }

        if (dto.getDataExpiracao() == null) {
            throw new IllegalArgumentException("Informe a data de expiração da promoção!");
        }

        if (dto.getDataExpiracao().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de expiração não pode ser anterior à data atual!");
        }
    }

    public void delete(Integer id) {
        PromocaoModel promocao = findById(id);
        if (promocao == null) {
            throw new RuntimeException("Promoção não encontrada para exclusão!");
        }
        promocaoRepository.delete(promocao);
    }
}