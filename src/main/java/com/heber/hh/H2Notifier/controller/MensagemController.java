package com.heber.hh.H2Notifier.controller;

import com.heber.hh.H2Notifier.dto.ApiResponse;
import com.heber.hh.H2Notifier.dto.MensagemRequest;
import com.heber.hh.H2Notifier.dto.MensagemResponse;
import com.heber.hh.H2Notifier.model.Mensagem;
import com.heber.hh.H2Notifier.service.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mensagens")
public class MensagemController {

    private final ModelMapper modelMapper;
    private final MensagemService mensagemService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MensagemResponse>>> listarTodas (
            @PageableDefault(size = 10, sort = "id")Pageable pageable){

        Page<Mensagem> mensagensPage = mensagemService.listarTodas(pageable);
        Page<MensagemResponse> responsePage = mensagensPage.map( mensagem -> modelMapper.map(mensagem, MensagemResponse.class));
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista de mensagens obtida com sucesso", responsePage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MensagemResponse>> buscarPorId(@PathVariable Long id){
        Mensagem mensagem = mensagemService.buscarMensagemPorId(id);
        MensagemResponse response = modelMapper.map(mensagem, MensagemResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mensagem obtida com sucesso", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MensagemResponse>> criarMensagem (@Valid @RequestBody MensagemRequest request){
        Mensagem template = mensagemService.salvarMensagem(request.getTitulo(), request.getCanal(), request.getConteudo());
        MensagemResponse response = modelMapper.map(template, MensagemResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mensagem cadastrada com sucesso", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MensagemResponse>> atualizarMensagem (@PathVariable Long id, @Valid @RequestBody MensagemRequest request){
        Mensagem template = mensagemService.atualizarMensagem(id, request.getTitulo(), request.getConteudo(), request.getCanal());
        MensagemResponse response = modelMapper.map(template, MensagemResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mensagem atualizada com sucesso.", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <ApiResponse<Void>> deletarMensagem (@PathVariable Long id){
        mensagemService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }

}
