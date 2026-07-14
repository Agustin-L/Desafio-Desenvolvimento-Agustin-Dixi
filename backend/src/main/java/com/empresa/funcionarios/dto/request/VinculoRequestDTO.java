package com.empresa.funcionarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VinculoRequestDTO {

    @NotBlank(message = "A empresa é obrigatória.")
    private String empresa;

    @NotBlank(message = "A matrícula é obrigatória.")
    private String matricula;

    @NotNull(message = "Selecione o funcionário.")
    private Long funcionarioId;

    @NotNull(message = "Selecione o cargo.")
    private Long cargoId;

    @NotNull(message = "Selecione o departamento.")
    private Long departamentoId;

    public VinculoRequestDTO() {
    }

    public VinculoRequestDTO(String empresa, String matricula, Long funcionarioId, Long cargoId, Long departamentoId) {
        this.empresa = empresa;
        this.matricula = matricula;
        this.funcionarioId = funcionarioId;
        this.cargoId = cargoId;
        this.departamentoId = departamentoId;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }
}
