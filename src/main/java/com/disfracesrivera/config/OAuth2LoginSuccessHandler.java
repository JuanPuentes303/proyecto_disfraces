package com.disfracesrivera.config;

import com.disfracesrivera.service.CustomUserDetailsService;
import com.disfracesrivera.service.OAuth2UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2UsuarioService oAuth2UsuarioService;
    private final CustomUserDetailsService customUserDetailsService;

    public OAuth2LoginSuccessHandler(
            OAuth2UsuarioService oAuth2UsuarioService,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.oAuth2UsuarioService = oAuth2UsuarioService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            org.springframework.security.core.Authentication authentication
    ) throws IOException, ServletException {

        if (!(authentication.getPrincipal() instanceof OAuth2User oauth2User)) {
            response.sendRedirect("/");
            return;
        }

        String correo = oauth2User.getAttribute("email");

        if (correo == null || correo.isBlank()) {
            response.sendRedirect("/login?error");
            return;
        }

        correo = correo.toLowerCase().trim();

        oAuth2UsuarioService.crearUsuarioOAuthSiNoExiste(oauth2User);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(correo);

        UsernamePasswordAuthenticationToken nuevaAutenticacion =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(nuevaAutenticacion);

        request.getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        response.sendRedirect("/");
    }
}