package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.model.Canal;
import com.heber.hh.H2Notifier.model.Parcela;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificacaoScheduler {
    private final ParcelaService parcelaService;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final NotificacaoService notificacaoService;

    @Scheduled(cron = "0 0 9 * * *")
    public void notificarParcelasVencidas(){
        List<Parcela> vencidas = parcelaService.listarVencidas();
        for (Parcela parcela:vencidas){
            parcela.getDevedores().forEach(devedor -> {
                String mensagem = String.format("Olá %s, a parcela '%s' venceu em %s. Valor: R$ %s.",
                        devedor.getNome(), parcela.getDescricao(), parcela.getVencimento(), parcela.getValorParcelaCada());
                boolean enviado = false;

                if(devedor.getTelefone() != null){
                    enviado = whatsAppService.enviar(devedor.getTelefone(), mensagem);
                    notificacaoService.registrarNotificacao(parcela.getCobrador(), devedor, parcela, mensagem, Canal.WHATSAPP, enviado);
                }

                if(devedor.getEmail() != null){
                    try{
                        emailService.enviar(parcela.getCobrador().getEmail(), devedor.getEmail(), "Parcela Vencida", mensagem);
                        notificacaoService.registrarNotificacao(parcela.getCobrador(), devedor, parcela, mensagem, Canal.EMAIL, true);
                    } catch (Exception e){
                        notificacaoService.registrarNotificacao(parcela.getCobrador(), devedor, parcela, mensagem, Canal.EMAIL, false);
                    }
                }
            });
        }
    }
}
