package com.heber.hh.H2Notifier.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Notificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pessoa cobrador;

    @ManyToOne(optional = false)
    private Pessoa devedor;

    @ManyToOne(optional = false)
    private Parcela parcela;

    private LocalDateTime horaNotificacao;

    @Enumerated(EnumType.STRING)
    private Canal canal;

    private boolean sucesso;

    @Column(columnDefinition = "TEXT")
    String mensagemEnviada;
}
