package com.heber.hh.H2Notifier.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
public class ParcelaRequest {
    @NotBlank (message = "Descrição da parcela não pode estar vazia.")
    private String descricao;
    @NotNull(message = "Data de vencimento é obrigatória.")
    private LocalDate vencimento;
    @NotNull(message = "Valor total é obrigatório.")
    private BigDecimal valorTotal;
    private boolean quitada;
    private String chavePix;
    private String codigoPixCopyPaste;
    @NotNull (message = "Cobrador id é obrigatorio")
    private Long idCobrador;
    private List<Long> idsDevedores;
}
