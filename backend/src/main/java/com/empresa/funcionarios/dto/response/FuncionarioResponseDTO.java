package com.empresa.funcionarios.dto.response;

public class FuncionarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private Boolean ativo;

    public FuncionarioResponseDTO() {
    }

    public FuncionarioResponseDTO(Long id, String nome, String cpf, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}