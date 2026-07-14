package com.empresa.funcionarios.dto.request;

import com.empresa.funcionarios.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioRequestDTO {

    @NotBlank(message = "O usuário é obrigatório.")
    @Size(min = 3, message = "O usuário deve ter pelo menos 3 caracteres.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String senha;

    @NotNull(message = "O perfil é obrigatório.")
    private Perfil perfil;

    public UsuarioRequestDTO() {
    }

    public UsuarioRequestDTO(String username, String senha, Perfil perfil) {
        this.username = username;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}
