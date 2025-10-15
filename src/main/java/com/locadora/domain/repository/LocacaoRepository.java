package com.locadora.domain.repository;

import com.locadora.domain.entity.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocacaoRepository extends JpaRepository<Locacao, Long> {
    java.util.List<Locacao> findByUsuarioId(Long usuarioId);
    java.util.List<Locacao> findByVeiculoId(Long veiculoId);
    java.util.List<Locacao> findByStatus(String status);
    java.util.List<Locacao> findByDataFimBefore(java.time.LocalDate date);
    boolean existsByVeiculoIdAndStatus(Long veiculoId, String status);
}
