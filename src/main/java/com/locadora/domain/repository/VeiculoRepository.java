package com.locadora.domain.repository;

import com.locadora.domain.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.locadora.domain.enums.StatusVeiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa);

    // Derived query to find vehicles by status
    List<Veiculo> findByStatus(StatusVeiculo status);
}
