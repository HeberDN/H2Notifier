package com.heber.hh.H2Notifier.controller;

import com.heber.hh.H2Notifier.dto.ApiResponse;
import com.heber.hh.H2Notifier.dto.PessoaRequest;
import com.heber.hh.H2Notifier.dto.PessoaResponse;
import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.model.TipoPessoa;
import com.heber.hh.H2Notifier.service.PessoaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
@Slf4j
public class PessoaController {
    private final PessoaService pessoaService;

    @PostMapping
    public ResponseEntity<ApiResponse<PessoaResponse>> criar (@Valid @RequestBody PessoaRequest request){
        PessoaResponse response = pessoaService.salvarNovaPessoa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Pessoa cadasdastrada com sucesso.", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar (@PathVariable Long id){
        log.info("Tentado deletar pessoa com id: {}", id);
        pessoaService.deletarPessoa(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoaResponse>> editar(@PathVariable Long id, @Valid @RequestBody PessoaRequest request){
        log.info("Tentado editar pessoa com id: {}", id);
        PessoaResponse response = pessoaService.editarPessoa(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Pessoa alterada com sucesso.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PessoaResponse>>> listarTodas (){
        List<PessoaResponse> pessoas = pessoaService.listarTodasAsPessoas();
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista obtida com sucesso.",pessoas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pessoa>> buscarPorId(@PathVariable Long id){
        Pessoa response = pessoaService.buscarPessoaPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Pessoa obtida com sucesso", response));
    }

    @GetMapping("/tipoPessoa/{tipoPessoa}")
    public ResponseEntity<ApiResponse<List<PessoaResponse>>> buscarPessoaPorTipoPessoa(@PathVariable TipoPessoa tipoPessoa){
        List<PessoaResponse> listaTipoPessoas = pessoaService.listarPessoasPorTipoPessoa(tipoPessoa);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista obtida com sucesso.",listaTipoPessoas));
    }
}
