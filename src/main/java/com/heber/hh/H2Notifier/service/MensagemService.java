package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.exceptions.ResourceNotFoundException;
import com.heber.hh.H2Notifier.model.Canal;
import com.heber.hh.H2Notifier.model.Mensagem;
import com.heber.hh.H2Notifier.model.Parcela;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.repository.MensagemRepository;
import com.heber.hh.H2Notifier.utilities.TemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensagemService {

    private final MensagemRepository mensagemRepository;

    public Page<Mensagem> listarTodas (Pageable pageable){
        return mensagemRepository.findAll(pageable);
    }

    public Mensagem buscarMensagemPorId(Long id) {
        return mensagemRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Mensagem não encontrada com id: "+id));
    }

    public Mensagem salvarMensagem (String titulo, Canal canal, String conteudo){
        Mensagem mensagem = new Mensagem();
        mensagem.setTitulo(titulo);
        mensagem.setConteudo(conteudo);
        mensagem.setCanal(canal);
        return mensagemRepository.save(mensagem);
    }

    public Mensagem atualizarMensagem (Long id, String titulo, String conteudo, Canal canal){
        Mensagem mensagemExistente = buscarMensagemPorId(id);
        mensagemExistente.setCanal(canal);
        mensagemExistente.setTitulo(titulo);
        mensagemExistente.setConteudo(conteudo);
        return mensagemRepository.save(mensagemExistente);
    }

    public void deletarMensagem (Long id){
        buscarMensagemPorId(id);
        mensagemRepository.deleteById(id);
    }

    public String gerarMensagem (String mensagemPessonalizada, Canal canal, Parcela parcela, Pessoa devedor){
        String template = mensagemPessonalizada;
        log.info("template recebido através do mensagemPessonalizada: {}", template);

        if (template == null || template.isEmpty()){
            log.info("template é null ou esta vazio: {}", template);
            template = mensagemRepository.findByCanal(canal)
                    .map(Mensagem :: getConteudo)
                    .orElse("Olá {nome.devedor}, a parcela \"{descricao.parcela}\" vence em {data.vencimento}. Valor total: R$ {valor.total}");
            log.info("template padrão: {}", template);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Map<String, String> variaveis = new HashMap<>();
        variaveis.put("nome.devedor", devedor.getNome());
        variaveis.put("descricao.parcela", parcela.getDescricao());
        variaveis.put("data.vencimento", parcela.getVencimento().format(formatter));
        variaveis.put("dias.para.vencimento", String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), parcela.getVencimento())));
        variaveis.put("valor.total", String.format("%.2f", parcela.getValorTotal()));
        variaveis.put("valor.cada", String.format("%.2f", parcela.getValorParcelaCada()));
        if(parcela.getChavePix() != null){
            log.info("chave pix na mensagem parcela: {}", parcela.getChavePix());
            variaveis.put("chave.pix", parcela.getChavePix());
        }
        template = TemplateProcessor.process(template, variaveis);
        log.info("template processado: {}", template);
        return template;
    }
}
