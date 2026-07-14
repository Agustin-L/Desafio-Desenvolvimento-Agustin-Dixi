package com.empresa.funcionarios.repository;

import com.empresa.funcionarios.model.Vinculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VinculoRepository extends JpaRepository<Vinculo, Long> {

    List<Vinculo> findByFuncionarioId(Long funcionarioId);
}
