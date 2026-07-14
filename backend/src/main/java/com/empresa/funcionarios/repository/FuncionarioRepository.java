package com.empresa.funcionarios.repository;

import com.empresa.funcionarios.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    boolean existsByCpf(String cpf);

    @Query("SELECT DISTINCT f FROM Funcionario f LEFT JOIN Vinculo v ON v.funcionario = f " +
            "WHERE (:nome IS NULL OR LOWER(f.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:cpf IS NULL OR f.cpf LIKE CONCAT('%', :cpf, '%')) " +
            "AND (:matricula IS NULL OR v.matricula LIKE CONCAT('%', :matricula, '%')) " +
            "AND (:empresa IS NULL OR LOWER(v.empresa) LIKE LOWER(CONCAT('%', :empresa, '%'))) " +
            "AND (:cargoId IS NULL OR v.cargo.id = :cargoId) " +
            "AND (:departamentoId IS NULL OR v.departamento.id = :departamentoId) " +
            "ORDER BY f.nome")
    List<Funcionario> buscarComFiltros(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("matricula") String matricula,
            @Param("empresa") String empresa,
            @Param("cargoId") Long cargoId,
            @Param("departamentoId") Long departamentoId
    );
}
