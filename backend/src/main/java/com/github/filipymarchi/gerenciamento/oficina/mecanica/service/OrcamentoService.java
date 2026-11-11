package com.github.filipymarchi.gerenciamento.oficina.mecanica.service;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.dto.OrcamentoDto;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.*;
import com.github.filipymarchi.gerenciamento.oficina.mecanica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final RevisaoRepository revisaoRepository;
    private final ServicoRepository servicoRepository;
    private final PecaRepository pecaRepository;
    private final FluidoRepository fluidoRepository;

    public List<OrcamentoModel> findAll() {
        return orcamentoRepository.findAll();
    }

    public OrcamentoModel findById(Integer id) {
        return orcamentoRepository.findById(id).orElse(null);
    }

    public OrcamentoModel findByRevisaoId(Integer revisaoId) {
        return orcamentoRepository.findByRevisaoId(revisaoId).orElse(null);
    }

    public void save(OrcamentoDto dto) {
        validar(dto);

        RevisaoModel revisao = revisaoRepository.findById(dto.getRevisaoId())
                .orElseThrow(() -> new RuntimeException("Revisão não encontrada!"));

        List<ServicoModel> servicos = servicoRepository.findAllById(dto.getServicosIds());
        List<PecaModel> pecas = pecaRepository.findAllById(dto.getPecasIds());
        List<FluidoModel> fluidos = fluidoRepository.findAllById(dto.getFluidosIds());

        // Calcula o valor total somando todos os itens
        BigDecimal valorTotalCalculado = calcularValorTotal(servicos, pecas, fluidos);

        orcamentoRepository.save(OrcamentoModel.builder()
                .valorTotal(valorTotalCalculado)
                .revisao(revisao)
                .servicos(servicos)
                .pecasUtilizadas(pecas)
                .fluidosUtilizados(fluidos)
                .build());
    }

    public OrcamentoModel update(Integer id, OrcamentoDto dto) {
        validar(dto);

        OrcamentoModel orcamento = findById(id);
        if (orcamento == null) {
            throw new RuntimeException("Orçamento não encontrado para atualização!");
        }

        RevisaoModel revisao = revisaoRepository.findById(dto.getRevisaoId())
                .orElseThrow(() -> new RuntimeException("Revisão não encontrada!"));

        List<ServicoModel> servicos = servicoRepository.findAllById(dto.getServicosIds());
        List<PecaModel> pecas = pecaRepository.findAllById(dto.getPecasIds());
        List<FluidoModel> fluidos = fluidoRepository.findAllById(dto.getFluidosIds());

        // Recalcula o valor total para a atualização
        BigDecimal valorTotalCalculado = calcularValorTotal(servicos, pecas, fluidos);

        orcamento.setValorTotal(valorTotalCalculado);
        orcamento.setRevisao(revisao);
        orcamento.setServicos(servicos);
        orcamento.setPecasUtilizadas(pecas);
        orcamento.setFluidosUtilizados(fluidos);

        return orcamentoRepository.save(orcamento);
    }

    /**
     * Função auxiliar para somar os valores de serviços, peças e fluidos (1 unidade de cada item selecionado).
     */
    private BigDecimal calcularValorTotal(List<ServicoModel> servicos, List<PecaModel> pecas, List<FluidoModel> fluidos) {
        BigDecimal total = BigDecimal.ZERO;

        for (ServicoModel servico : servicos) {
            total = total.add(BigDecimal.valueOf(servico.getValor()));
        }

        for (PecaModel peca : pecas) {
            total = total.add(BigDecimal.valueOf(peca.getValor()));
        }

        for (FluidoModel fluido : fluidos) {
            total = total.add(BigDecimal.valueOf(fluido.getValor()));
        }

        return total;
    }

    private void validar(OrcamentoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do orçamento não informados!");
        }

        if (dto.getRevisaoId() == null) {
            throw new IllegalArgumentException("Informe a revisão associada ao orçamento!");
        }
    }

    public void delete(Integer id) {
        OrcamentoModel orcamento = findById(id);
        if (orcamento == null) {
            throw new RuntimeException("Orçamento não encontrado para exclusão!");
        }
        orcamentoRepository.delete(orcamento);
    }
}