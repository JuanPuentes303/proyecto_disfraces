package com.disfracesrivera.controller;

import com.disfracesrivera.dto.RegistroRequest;
import com.disfracesrivera.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroRequest", new RegistroRequest());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @Valid RegistroRequest registroRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "registro";
        }

        try {
            usuarioService.registrarUsuario(registroRequest);
            return "redirect:/login?registroExitoso";
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorRegistro", e.getMessage());
            return "registro";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
}