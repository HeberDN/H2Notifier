package com.heber.hh.H2Notifier.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ParcelaResponse {
    private Long id;
    private String descricao;
    private LocalDate vencimento;
    private BigDecimal valorTotal;
    private BigDecimal valorParcelaCada;
    private boolean quitada;
    private String chavePix;
    private String codigoPixCopyPaste;
    private PessoaResponse cobrador;
    private List<PessoaResponse> devedores;
}
