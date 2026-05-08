package com.disfracesrivera.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginApiRequest {

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getCorreo() {
        return correo;
    }

    public String getPassword() {
        return password;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}