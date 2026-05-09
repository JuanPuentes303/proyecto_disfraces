package com.disfracesrivera.service;

import com.disfracesrivera.model.Rol;
import com.disfracesrivera.model.Usuario;
import com.disfracesrivera.repository.RolRepository;
import com.disfracesrivera.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class OAuth2UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void crearUsuarioOAuthSiNoExiste(OAuth2User oauth2User) {
        String correo = oauth2User.getAttribute("email");

        if (correo == null || correo.isBlank()) {
            throw new IllegalArgumentException("No se pudo obtener el correo desde Google");
        }

        String correoNormalizado = correo.toLowerCase().trim();

        if (usuarioRepository.findByCorreo(correoNormalizado).isPresent()) {
            return;
        }

        Rol rolUser = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new IllegalArgumentException("El rol ROLE_USER no existe"));

        String nombre = oauth2User.getAttribute("given_name");
        String apellido = oauth2User.getAttribute("family_name");

        if (nombre == null || nombre.isBlank()) {
            nombre = oauth2User.getAttribute("name");
        }

        if (nombre == null || nombre.isBlank()) {
            nombre = "Usuario";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido != null ? apellido : "");
        usuario.setCorreo(correoNormalizado);
        usuario.setTelefono("");
        usuario.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        usuario.setActivo(true);
        usuario.setRoles(Set.of(rolUser));

        usuarioRepository.save(usuario);
    }
}