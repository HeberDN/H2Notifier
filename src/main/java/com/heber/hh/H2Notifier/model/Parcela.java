package com.heber.hh.H2Notifier.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Parcela {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private LocalDate vencimento;
    private BigDecimal valorTotal;
    private BigDecimal valorParcelaCada;
    private boolean quitada;
    private String chavePix;
    private String codigoPixCopyPaste;

    @ManyToOne
    private Pessoa cobrador;

    @ManyToMany
    private List<Pessoa> devedores = new ArrayList<>();
}
