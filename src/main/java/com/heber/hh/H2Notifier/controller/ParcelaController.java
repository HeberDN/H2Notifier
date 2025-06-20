package com.heber.hh.H2Notifier.controller;

import com.heber.hh.H2Notifier.dto.ApiResponse;
import com.heber.hh.H2Notifier.dto.ParcelaRequest;
import com.heber.hh.H2Notifier.dto.ParcelaResponse;
import com.heber.hh.H2Notifier.model.Parcela;
import com.heber.hh.H2Notifier.service.ParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parcelas")
@RequiredArgsConstructor
public class ParcelaController {
    private final ParcelaService parcelaService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ParcelaResponse>> criar (@Valid @RequestBody ParcelaRequest request){
        Parcela parcela = parcelaService.salvarNovaParcela(request);
        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Parcela criada com sucesso.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ParcelaResponse>> obterParcelaPorId (@PathVariable Long id){
        Parcela parcela = parcelaService.buscarParcelaPeloId(id);
        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcela obtida com sucesso", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ParcelaResponse>>> listarTodas(){
        List<Parcela> parcelas = parcelaService.listarTodas();
        List<ParcelaResponse> lista = parcelas.stream().map(parcela -> modelMapper.map(parcela, ParcelaResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Lista obtida com sucesso", lista));
    }

    @PutMapping("/{id}")
    public ResponseEntity <ApiResponse<ParcelaResponse>> atualizarParcela (@PathVariable Long id, @Valid @RequestBody ParcelaRequest request){
        Parcela parcela = parcelaService.editarParcela(id, request);
        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcela atualizada com sucesso", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarParcela (@PathVariable Long id){
        parcelaService.deletarParcela(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ApiResponse<>(true, "Parcela excluida com sucesso"));
    }

    @GetMapping("/cobrador/{idCobrador}")
    public ResponseEntity<ApiResponse<List<ParcelaResponse>>> listarPorCobrador (@PathVariable Long idCobrador){
        List<Parcela> parcelas = parcelaService.listarPorCobrador(idCobrador);
        List<ParcelaResponse> lista = parcelas.stream().map(parcela -> modelMapper.map(parcela, ParcelaResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcelas do cobrador obtidas com sucesso", lista));
    }

    @GetMapping("/devedor/{idDevedor}")
    public ResponseEntity<ApiResponse<List<ParcelaResponse>>> listarPorDevedor(@PathVariable Long idDevedor){
        List<Parcela> parcelas = parcelaService.listarPorDevedor(idDevedor);
        List<ParcelaResponse> lista = parcelas.stream().map(parcela -> modelMapper.map(parcela, ParcelaResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcelas do devedor obtidas com suscesso", lista));
    }

    @GetMapping("/vencidas")
    public ResponseEntity<ApiResponse<List<ParcelaResponse>>> listarVencidas(){
        List<Parcela> parcelas = parcelaService.listarVencidas();
        List<ParcelaResponse> lista = parcelas.stream().map(parcela -> modelMapper.map(parcela, ParcelaResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcelas vencidas obtidas com sucesso", lista));
    }

    @GetMapping("/vencimento/{vencimento}")
    public ResponseEntity<ApiResponse<List<ParcelaResponse>>> listarParcelasPorVencimento(@PathVariable LocalDate vencimento){
        List<Parcela> parcelas = parcelaService.listarParcelasPorVencimento(vencimento);
        List<ParcelaResponse> lista = parcelas.stream().map(parcela -> modelMapper.map(parcela, ParcelaResponse.class)).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcelas obtidas com sucesso para vencimento: " + vencimento, lista));
    }

    @PutMapping("/{id}/quitar")
    public ResponseEntity<ApiResponse<ParcelaResponse>> quitarParcela(@PathVariable Long id){
        Parcela parcela = parcelaService.quitarParcela(id);
        ParcelaResponse response = modelMapper.map(parcela, ParcelaResponse.class);
        return ResponseEntity.ok(new ApiResponse<>(true, "Parcela quitada com sucesso.", response));
    }

    @GetMapping("/total-a-receber/{idCobrador}")
    public ResponseEntity <ApiResponse<BigDecimal>> obterTotalAReceber(@PathVariable Long idCobrador){
        BigDecimal total = parcelaService.calcularTotalAReceber(idCobrador);
        return ResponseEntity.ok(new ApiResponse<>(true, "Total a receber calculado com sucesso", total));
    }
}
