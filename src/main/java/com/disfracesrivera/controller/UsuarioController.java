package com.disfracesrivera.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    @GetMapping("/usuario/reservas")
    public String misReservas() {
        return "mis-reservas";
    }
}