package com.disfracesrivera.service;

import com.disfracesrivera.model.Usuario;
import com.disfracesrivera.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo.toLowerCase().trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<GrantedAuthority> authorities = usuario.getRoles()
                .stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toSet());

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(authorities)
                .disabled(!usuario.getActivo())
                .build();
    }
}