package com.heber.hh.H2Notifier.dto;

import com.heber.hh.H2Notifier.model.Canal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MensagemRequest {
    @NotBlank(message = "Titulo do Template é obrigatório.")
    private String titulo;
    @NotBlank(message = "Conteudo do Template é obrigatório.")
    private String conteudo;
    @NotNull(message = "Canal do Template é obrigatório.")
    private Canal canal;
}
