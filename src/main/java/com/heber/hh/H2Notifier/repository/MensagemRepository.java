package com.heber.hh.H2Notifier.repository;

import com.heber.hh.H2Notifier.model.Canal;
import com.heber.hh.H2Notifier.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    Optional<Mensagem> findByCanal(Canal canal);
}
