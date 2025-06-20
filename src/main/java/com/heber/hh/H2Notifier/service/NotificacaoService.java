package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.dto.NotificacaoRequest;
import com.heber.hh.H2Notifier.exceptions.NotificacaoException;
import com.heber.hh.H2Notifier.model.Canal;
import com.heber.hh.H2Notifier.model.Notificacao;
import com.heber.hh.H2Notifier.model.Parcela;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final MensagemService mensagemService;
    private final WhatsAppService whatsAppService;
    private final EmailService emailService;
    private final ParcelaService parcelaService;

    public boolean enviarParaCanais(NotificacaoRequest request) {
        Parcela parcela = parcelaService.buscarParcelaPeloId(request.getIdParcela());
        Pessoa cobrador = parcela.getCobrador();
        List<Pessoa> devedores = parcela.getDevedores();

        boolean houveFalha = false;

        for (Pessoa devedor : devedores) {
            for (Canal canal : request.getCanais()) {
                String mensagem = mensagemService.gerarMensagem(request.getMensagemPersonalizada(), canal, parcela, devedor);
                boolean sucesso = enviarMensagem(canal, cobrador, devedor, mensagem, parcela);
                if (!sucesso) houveFalha = true;
                registrarNotificacao(cobrador, devedor, parcela, mensagem, canal, sucesso);
            }
        }

        return houveFalha;
    }

    private boolean enviarMensagem(Canal canal, Pessoa cobrador, Pessoa devedor, String mensagem, Parcela parcela) {
        try {
            if (canal == Canal.WHATSAPP && devedor.getTelefone() != null) {
                boolean sucessoPrincipal = whatsAppService.enviar(devedor.getTelefone(), mensagem);
                if (!sucessoPrincipal) {
                    log.error("Falha ao enviar mensagem principal para {}", devedor.getTelefone());
                    return false;
                }
                String codigoPix = checkCodigoPixCopyPaste(cobrador, parcela);
                if(codigoPix != null){
                    whatsAppService.enviar(devedor.getTelefone(), "Segue Pix Copia e Cola");
                    boolean sucessoPix = whatsAppService.enviar(devedor.getTelefone(), codigoPix);
                    log.info("PIX copia-e-cola enviado para {}: {}", devedor.getTelefone(), codigoPix);
                    return sucessoPix || codigoPix.isEmpty();
                }
                return true;
            } else if (canal == Canal.EMAIL && devedor.getEmail() != null) {
                emailService.enviar(cobrador.getEmail(), devedor.getEmail(), "Lembrete de parcela", mensagem);
                return true;
            }
        } catch (Exception e) {
            throw new NotificacaoException("Erro enviar notificação: " + e.getMessage());
        }
        return false;
    }

    private String checkCodigoPixCopyPaste(Pessoa cobrador, Parcela parcela){

        String codigoPixCopyPaste = parcelaService.obterCodigoCopyPastePix(cobrador, parcela.getCodigoPixCopyPaste());
        if(codigoPixCopyPaste != null && !codigoPixCopyPaste.isBlank()){
            return codigoPixCopyPaste;
        }
        return null;
    }

    public void registrarNotificacao (Pessoa cobrador, Pessoa devedor, Parcela parcela, String mensagem, Canal canal, boolean sucesso){
        Notificacao notificacao = new Notificacao();
        notificacao.setCanal(canal);
        notificacao.setCobrador(cobrador);
        notificacao.setDevedor(devedor);
        notificacao.setParcela(parcela);
        notificacao.setHoraNotificacao(LocalDateTime.now());
        notificacao.setMensagemEnviada(mensagem);
        notificacao.setSucesso(sucesso);

        notificacaoRepository.save(notificacao);
    }
}
