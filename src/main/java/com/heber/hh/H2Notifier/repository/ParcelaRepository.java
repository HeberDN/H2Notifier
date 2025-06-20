package com.heber.hh.H2Notifier.repository;

import com.heber.hh.H2Notifier.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findByVencimento(LocalDate vencimento);

    List<Parcela> findByCobradorId(Long idCobrador);

    List<Parcela> findByVencimentoBeforeAndQuitadaFalse(LocalDate now);

    List<Parcela> findByCobradorIdAndQuitadaFalse(Long idCobrador);

    @Query("SELECT p FROM Parcela p JOIN p.devedores d WHERE d.id = :idDevedor")
    List<Parcela> findByDevedorId(@Param("idDevedor") Long idDevedor);
}
