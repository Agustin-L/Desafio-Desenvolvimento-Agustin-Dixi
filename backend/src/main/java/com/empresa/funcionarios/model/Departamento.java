package com.empresa.funcionarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departamento")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    public Departamento(){
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setCodigo(String codigo){
        this.codigo = codigo;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

}