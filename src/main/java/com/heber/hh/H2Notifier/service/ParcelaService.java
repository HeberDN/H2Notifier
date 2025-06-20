package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.dto.ParcelaRequest;
import com.heber.hh.H2Notifier.dto.ParcelaResponse;
import com.heber.hh.H2Notifier.exceptions.ParcelaException;
import com.heber.hh.H2Notifier.exceptions.ResourceNotFoundException;
import com.heber.hh.H2Notifier.model.Parcela;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.model.TipoPessoa;
import com.heber.hh.H2Notifier.repository.ParcelaRepository;
import com.heber.hh.H2Notifier.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    public Parcela salvarNovaParcela (ParcelaRequest request){
        Pessoa cobrador = buscarCobrador(request.getIdCobrador());

        List<Pessoa> devedores = new ArrayList<>();
        if (request.getIdsDevedores() != null){
            devedores = pessoaRepository.findAllById(request.getIdsDevedores());
        }

        if(devedores.isEmpty()){
            throw new ParcelaException("É necessário informar pelo menos um devedor válido para criar a parcela.");
        }
        List<Long> idsDevedores = request.getIdsDevedores();
        if(devedores.size() != idsDevedores.size()){
            List<Long> idsEncontrados = devedores.stream()
                    .map(Pessoa :: getId)
                    .toList();
            List<Long> idsNaoEncontrados = idsDevedores.stream()
                    .filter(id -> !idsEncontrados.contains(id))
                    .toList();
            throw new ParcelaException("O(s) seguinte(s) id(s) não foi(ram) encontrado(s) para o(s) devedor(es): " + idsNaoEncontrados);
        }

        Parcela parcela = new Parcela();
        parcela.setDescricao(request.getDescricao());
        parcela.setVencimento(request.getVencimento());
        parcela.setValorTotal(request.getValorTotal());
        parcela.setQuitada(request.isQuitada());
        parcela.setChavePix(request.getChavePix());
        parcela.setCodigoPixCopyPaste(request.getCodigoPixCopyPaste());
        parcela.setCobrador(cobrador);
        parcela.setDevedores(devedores);

        parcela.setChavePix(obterChavePix(cobrador, request.getChavePix()));
        parcela.setCodigoPixCopyPaste(obterCodigoCopyPastePix(cobrador, request.getCodigoPixCopyPaste()));

        parcela.setValorParcelaCada(calcularValorPorPessoa(parcela));

        return parcelaRepository.save(parcela);
    }

    public Parcela buscarParcelaPeloId (Long id){
        return parcelaRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Parcela não encontrada com id: " + id) );
    }

    public Parcela editarParcela (Long id, ParcelaRequest request){
        Parcela parcelaExistente = buscarParcelaPeloId(id);

        Pessoa cobrador = buscarCobrador(request.getIdCobrador());

        List<Pessoa> devedores = new ArrayList<>();
        if (request.getIdsDevedores() != null){
            devedores = pessoaRepository.findAllById(request.getIdsDevedores());
        }

        if(devedores.isEmpty()){
            throw new ParcelaException("É necessário informar pelo menos um devedor válido para atualizar a parcela.");
        }

        List<Long> idsDevedores = request.getIdsDevedores();
        if(devedores.size() != idsDevedores.size()){
            List<Long> idsEncontrados = devedores.stream()
                    .map(Pessoa :: getId)
                    .toList();
            List<Long> idsNaoEncontrados = idsDevedores.stream()
                    .filter(devedorId -> !idsEncontrados.contains(devedorId))
                    .toList();
            throw new ParcelaException("O(s) seguinte(s) id(s) não foi(ram) encontrado(s) para o(s) devedor(es): " + idsNaoEncontrados);
        }

        parcelaExistente.setDescricao(request.getDescricao());
        parcelaExistente.setVencimento(request.getVencimento());
        parcelaExistente.setValorTotal(request.getValorTotal());
        parcelaExistente.setQuitada(request.isQuitada());
        parcelaExistente.setChavePix(obterChavePix(cobrador, request.getChavePix()));
        parcelaExistente.setCobrador(cobrador);
        parcelaExistente.setDevedores(devedores);


        parcelaExistente.setValorParcelaCada(calcularValorPorPessoa(parcelaExistente));

        return parcelaRepository.save(parcelaExistente);
    }

    public void deletarParcela (Long id){
        Parcela parcela = buscarParcelaPeloId(id);
        parcelaRepository.delete(parcela);
    }

    public List<Parcela> listarTodas (){
        return parcelaRepository.findAll();
    }

    public BigDecimal calcularValorPorPessoa(Parcela parcela){
        try {
            if(parcela.getDevedores() == null || parcela.getDevedores().isEmpty()){
                return parcela.getValorTotal();
            } else {
                int qtd = parcela.getDevedores().size();
                BigDecimal valor = parcela.getValorTotal().divide(BigDecimal.valueOf(qtd), 2, RoundingMode.HALF_UP);
                parcela.setValorParcelaCada(valor);
                return valor;
            }
        } catch (ParcelaException e) {
            throw new ParcelaException("Erro ao calcular valor por pessoa: " + e.getMessage());
        }
    }

    public String obterChavePix (Pessoa cobrador, String chavePixInformada){
        if (chavePixInformada != null && !chavePixInformada.isBlank()) {
            log.info("Usando chave Pix da parcela: {}", chavePixInformada);
            return chavePixInformada;
        }

        if (cobrador.getChavePix() != null && !cobrador.getChavePix().isBlank()) {
            log.info("Usando chave Pix do cobrador: {}", cobrador.getChavePix());
            return cobrador.getChavePix();
        }

        throw new ParcelaException("Nenhuma chave Pix válida informada na parcela ou obtido do cobrador.");
    }

    public String obterCodigoCopyPastePix(Pessoa cobrador, String codigoPixCopyPastInformado) {
        if(codigoPixCopyPastInformado != null && !codigoPixCopyPastInformado.isBlank()){
            log.info("Usando código Pix da parcela: {}", codigoPixCopyPastInformado);
            return codigoPixCopyPastInformado;
        }
        if (cobrador.getCodigoPixCopyPaste() != null && !cobrador.getCodigoPixCopyPaste().isBlank()){
            log.info("Usando código Pix do cobrador: {}", cobrador.getCodigoPixCopyPaste());
            return cobrador.getCodigoPixCopyPaste();
        }
        throw  new ParcelaException("Nenhuma código Pix válido informado na parcela ou obtido do cobrador.");
    }

    public List<Parcela> listarPorCobrador(Long idCobrador) {
        buscarCobrador(idCobrador);
        return parcelaRepository.findByCobradorId(idCobrador);
    }

    public List<Parcela> listarParcelasPorVencimento(LocalDate vencimento){
        return parcelaRepository.findByVencimento(vencimento);
    }

    public List<Parcela> listarVencidas() {
        return parcelaRepository.findByVencimentoBeforeAndQuitadaFalse(LocalDate.now());
    }

    public Parcela quitarParcela(Long id){
        Parcela parcela = buscarParcelaPeloId(id);
        parcela.setQuitada(true);
        return parcelaRepository.save(parcela);
    }

    public BigDecimal calcularTotalAReceber(Long idCobrador) {
        buscarCobrador(idCobrador);
        List<Parcela> parcelas = parcelaRepository.findByCobradorIdAndQuitadaFalse(idCobrador);

        return parcelas.stream()
                .map(Parcela :: getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal :: add);
    }

    public Pessoa buscarCobrador (Long idCobrador){
        Pessoa cobrador = pessoaRepository.findById(idCobrador)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrado com o id: " + idCobrador));
        if(cobrador.getTipoPessoa() != TipoPessoa.COBRADOR){
            throw new ParcelaException("Tipo Pessoa informada não é um COBRADOR");
        }
        return cobrador;
    }

    public List<Parcela> listarPorDevedor(Long idDevedor) {
        return parcelaRepository.findByDevedorId(idDevedor);
    }
}
