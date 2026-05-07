package com.disfracesrivera.service;

import com.disfracesrivera.dto.RegistroRequest;
import com.disfracesrivera.model.Rol;
import com.disfracesrivera.model.Usuario;
import com.disfracesrivera.repository.RolRepository;
import com.disfracesrivera.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(RegistroRequest request) {
        if (!request.getPassword().equals(request.getConfirmarPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("Ya existe una cuenta registrada con ese correo");
        }

        Rol rolUsuario = rolRepository.findByNombre("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("No existe el rol ROLE_USER"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setCorreo(request.getCorreo().toLowerCase().trim());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.getRoles().add(rolUsuario);

        usuarioRepository.save(usuario);
    }
}