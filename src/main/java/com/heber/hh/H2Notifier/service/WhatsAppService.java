package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.configurations.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {

    private final TwilioProperties twilioProperties;

    public boolean enviar(String numeroDestino, String mensagem){
        try {
            Twilio.init(twilioProperties.getAccountSid(), twilioProperties.getAuthToken());

            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + numeroDestino),
                    new com.twilio.type.PhoneNumber("whatsapp:" + twilioProperties.getFromNumber()),
                    mensagem
            ).create();
            log.info("Mensagem enviada: SID = {}", message.getAccountSid());

            log.info("Enviando mensagem WhatsApp para {}: {}",numeroDestino, mensagem);
            return true;
        } catch (Exception e) {
            log.error("Erro ao enviar mensagem WhatsApp: " + e.getMessage());
            return false;
        }
    }
}
