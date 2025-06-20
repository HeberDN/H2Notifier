package com.heber.hh.H2Notifier.service;

import com.heber.hh.H2Notifier.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void enviar(String from, String to, String assunto, String conteudo){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(assunto);
            helper.setText(conteudo, true);

            mailSender.send(message);
        } catch (MessagingException e){
            System.err.println("Erro interno do JavaMail: " + e.getMessage());
            e.printStackTrace();
            throw new EmailException("Falha ao enviar e-mail. Verifique as configurações ou os detalhes do destinatário.", e);
        }
    }
}
