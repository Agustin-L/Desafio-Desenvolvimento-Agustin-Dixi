package com.empresa.funcionarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "funcionario")
public class Funcionario{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    // Regra CLT: funcionário nunca é excluído, apenas inativado
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean ativo = true;

    public Funcionario(){
    }

    public Long getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}