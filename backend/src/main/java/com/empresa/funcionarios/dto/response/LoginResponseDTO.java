package com.empresa.funcionarios.dto.response;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String perfil;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, String username, String perfil) {
        this.token = token;
        this.username = username;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
