package com.heber.hh.H2Notifier.controller;

import com.heber.hh.H2Notifier.dto.ApiResponse;
import com.heber.hh.H2Notifier.dto.NotificacaoRequest;
import com.heber.hh.H2Notifier.model.Canal;
import com.heber.hh.H2Notifier.model.Parcela;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final ParcelaService parcelaService;
    private final NotificacaoService notificacaoService;
    private final MensagemService mensagemService;

    @PostMapping("/enviar")
    public ResponseEntity <ApiResponse<Void>> notificar (@RequestBody NotificacaoRequest request){
        boolean houveFalha = notificacaoService.enviarParaCanais(request);
        String mensagem = houveFalha ? "Notificações enviadas com falhas." : "Notificações enviadas com sucesso.";
        return ResponseEntity.ok(new ApiResponse<>(true, mensagem));
    }

    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<String>> previewMensagem(@RequestBody NotificacaoRequest request){
        Parcela parcela = parcelaService.buscarParcelaPeloId(request.getIdParcela());
        Pessoa devedor = parcela.getDevedores().get(0);
        Canal canal = request.getCanais() != null && !request.getCanais().isEmpty() ? request.getCanais().get(0) : Canal.EMAIL;
        String mensagemPreview = mensagemService.gerarMensagem(request.getMensagemPersonalizada(), canal, parcela, devedor);
        return ResponseEntity.ok(new ApiResponse<>(true, "Preview gerado com sucesso.", mensagemPreview));
    }

}
