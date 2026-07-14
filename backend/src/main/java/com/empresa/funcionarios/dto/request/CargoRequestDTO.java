package com.empresa.funcionarios.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CargoRequestDTO {

    @NotBlank(message = "O código do cargo é obrigatório.")
    private String codigo;

    @NotBlank(message = "A descrição do cargo é obrigatória.")
    private String descricao;

    public CargoRequestDTO() {
    }

    public CargoRequestDTO(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
