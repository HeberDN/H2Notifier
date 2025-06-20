package com.heber.hh.H2Notifier.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    @Enumerated(EnumType.STRING)
    private TipoPessoa tipoPessoa;
    private String chavePix;
    private String codigoPixCopyPaste;
}
