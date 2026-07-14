package com.empresa.funcionarios.dto.request;

import jakarta.validation.constraints.NotBlank;

public class DepartamentoRequestDTO {

    @NotBlank(message = "O código do departamento é obrigatório.")
    private String codigo;

    @NotBlank(message = "A descrição do departamento é obrigatória.")
    private String descricao;

    public DepartamentoRequestDTO() {
    }

    public DepartamentoRequestDTO(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
