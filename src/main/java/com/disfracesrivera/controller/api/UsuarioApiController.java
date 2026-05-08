package com.disfracesrivera.controller.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioApiController {

    @GetMapping("/perfil")
    public Map<String, Object> perfil(Authentication authentication) {
        return Map.of(
                "correo", authentication.getName(),
                "roles", authentication.getAuthorities()
        );
    }
}