package com.empresa.funcionarios.dto.response;

public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String perfil;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Long id, String username, String perfil) {
        this.id = id;
        this.username = username;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
