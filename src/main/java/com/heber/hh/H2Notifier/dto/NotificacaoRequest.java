package com.heber.hh.H2Notifier.dto;

import com.heber.hh.H2Notifier.model.Canal;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class NotificacaoRequest {
    @NotBlank(message = "idParcela é obrigatório.")
    private Long idParcela;
    private String mensagemPersonalizada;
    private List<Canal> canais;
}
