package com.disfracesrivera.service;

import com.disfracesrivera.model.Reserva;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.admin.email:}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoReservaAdmin(Reserva reserva) {
        if (adminEmail == null || adminEmail.isBlank()) {
            System.out.println("Correo admin no configurado. Se omite envío de correo de reserva.");
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();

            mensaje.setTo(adminEmail);
            mensaje.setSubject("Nueva reserva en Disfraces Rivera");
            mensaje.setText(construirMensajeReserva(reserva));

            mailSender.send(mensaje);
        } catch (MailException | IllegalArgumentException e) {
            System.err.println("No se pudo enviar el correo de reserva: " + e.getMessage());
        }
    }

    private String construirMensajeReserva(Reserva reserva) {
        String nombreCliente = reserva.getUsuario().getNombre();

        if (reserva.getUsuario().getApellido() != null && !reserva.getUsuario().getApellido().isBlank()) {
            nombreCliente += " " + reserva.getUsuario().getApellido();
        }

        String telefono = reserva.getUsuario().getTelefono() != null
                ? reserva.getUsuario().getTelefono()
                : "No registrado";

        String observaciones = reserva.getObservaciones() != null && !reserva.getObservaciones().isBlank()
                ? reserva.getObservaciones()
                : "Sin observaciones";

        return """
                Hola Administrador,

                Se ha realizado una nueva reserva en Disfraces Rivera.

                DATOS DEL CLIENTE
                Nombre: %s
                Correo: %s
                Teléfono: %s

                DATOS DEL DISFRAZ
                Disfraz: %s
                Categoría: %s
                Talla: %s
                Género: %s

                DATOS DE LA RESERVA
                Tipo: %s
                Fecha inicio: %s
                Fecha fin: %s
                Estado: %s
                Observaciones: %s

                Revisa el panel de administrador para gestionar esta reserva.
                """.formatted(
                nombreCliente,
                reserva.getUsuario().getCorreo(),
                telefono,
                reserva.getDisfraz().getNombre(),
                reserva.getDisfraz().getCategoria() != null
                        ? reserva.getDisfraz().getCategoria().getNombre()
                        : "Sin categoría",
                reserva.getDisfraz().getTalla(),
                reserva.getDisfraz().getGenero(),
                reserva.getTipo(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getEstado(),
                observaciones
        );
    }
}