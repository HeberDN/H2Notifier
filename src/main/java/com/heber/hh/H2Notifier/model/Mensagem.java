package com.heber.hh.H2Notifier.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Canal canal;
    @Column(columnDefinition = "TEXT")
    private String conteudo;
}