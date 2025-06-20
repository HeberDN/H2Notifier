package com.heber.hh.H2Notifier.repository;

import com.heber.hh.H2Notifier.model.Pessoa;
import com.heber.hh.H2Notifier.model.TipoPessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findAllByTipoPessoa(TipoPessoa tipoPessoa);
}
