package com.disfracesrivera.controller;

import com.disfracesrivera.service.ReservaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsuarioController {

    private final ReservaService reservaService;

    public UsuarioController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/usuario/reservas")
    public String misReservas(Authentication authentication, Model model) {
        model.addAttribute("reservas", reservaService.listarReservasPorCorreo(authentication.getName()));
        return "mis-reservas";
    }
}