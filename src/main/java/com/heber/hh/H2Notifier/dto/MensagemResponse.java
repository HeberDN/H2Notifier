package com.heber.hh.H2Notifier.dto;

import com.heber.hh.H2Notifier.model.Canal;
import lombok.Data;

@Data
public class MensagemResponse {
    private Long id;
    private String titulo;
    private Canal canal;
    private String conteudo;
}
