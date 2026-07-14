package com.empresa.funcionarios.dto.response;

public class VinculoResponseDTO {

    private Long id;

    private String empresa;
    private String matricula;

    private Long funcionarioId;
    private String funcionarioNome;

    private Long cargoId;
    private String cargoCodigo;

    private Long departamentoId;
    private String departamentoNome;

    public VinculoResponseDTO() {
    }

    public VinculoResponseDTO(
            Long id,
            String empresa,
            String matricula,
            Long funcionarioId,
            String funcionarioNome,
            Long cargoId,
            String cargoCodigo,
            Long departamentoId,
            String departamentoNome
    ) {
        this.id = id;
        this.empresa = empresa;
        this.matricula = matricula;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.cargoId = cargoId;
        this.cargoCodigo = cargoCodigo;
        this.departamentoId = departamentoId;
        this.departamentoNome = departamentoNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public Long getCargoId() {
        return cargoId;
    }

    public void setCargoId(Long cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoCodigo() {
        return cargoCodigo;
    }

    public void setCargoCodigo(String cargoCodigo) {
        this.cargoCodigo = cargoCodigo;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public String getDepartamentoNome() {
        return departamentoNome;
    }

    public void setDepartamentoNome(String departamentoNome) {
        this.departamentoNome = departamentoNome;
    }
}