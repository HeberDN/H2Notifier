package com.heber.hh.H2Notifier.dto;

import com.heber.hh.H2Notifier.model.TipoPessoa;
import lombok.Data;

@Data
public class PessoaResponse {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private TipoPessoa tipoPessoa;
    private String chavePix;
    private String codigoPixCopyPaste;
}
