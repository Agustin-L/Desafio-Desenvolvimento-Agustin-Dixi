package com.empresa.funcionarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vinculos")
public class Vinculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String empresa;

    @Column(nullable = false)
    private String matricula;

    @ManyToOne
    private Cargo cargo;

    @ManyToOne
    private Funcionario funcionario;

    @ManyToOne
    private Departamento departamento;

    public Vinculo() {
    }

    public Long getId() {
        return id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getMatricula() {
        return matricula;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}