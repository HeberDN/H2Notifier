package com.heber.hh.H2Notifier.dto;

import com.heber.hh.H2Notifier.model.TipoPessoa;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PessoaRequest {
    @NotBlank(message = "Nome não pode estar em branco ou null.")
    private String nome;
    @NotBlank(message = "E-mail não pode estar em branco ou null.")
    @Email(message = "Formato de e-mail inválido.")
    private String email;
    @NotBlank(message = "Telefone não pode estar em branco ou null.")
    private String telefone;
    private TipoPessoa tipoPessoa;
    private String chavePix;
    private String codigoPixCopyPaste;
}
