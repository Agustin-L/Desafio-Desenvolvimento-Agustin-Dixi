package com.empresa.funcionarios.repository;

import com.empresa.funcionarios.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    boolean existsByCodigo(String codigo);
}