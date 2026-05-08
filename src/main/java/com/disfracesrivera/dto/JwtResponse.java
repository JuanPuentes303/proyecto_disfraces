package com.disfracesrivera.dto;

public class JwtResponse {

    private String token;
    private String tipo = "Bearer";
    private String correo;

    public JwtResponse(String token, String correo) {
        this.token = token;
        this.correo = correo;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCorreo() {
        return correo;
    }
}