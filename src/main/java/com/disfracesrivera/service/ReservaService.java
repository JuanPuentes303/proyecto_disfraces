package com.disfracesrivera.service;

import com.disfracesrivera.dto.ReservaAdminView;
import com.disfracesrivera.dto.ReservaRequest;
import com.disfracesrivera.dto.ReservaView;
import com.disfracesrivera.model.Disfraz;
import com.disfracesrivera.model.EstadoReserva;
import com.disfracesrivera.model.Reserva;
import com.disfracesrivera.model.Usuario;
import com.disfracesrivera.repository.DisfrazRepository;
import com.disfracesrivera.repository.ReservaRepository;
import com.disfracesrivera.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final DisfrazRepository disfrazRepository;
    private final EmailService emailService;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            DisfrazRepository disfrazRepository,
            EmailService emailService
    ) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.disfrazRepository = disfrazRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void actualizarReservasVencidas() {
        reservaRepository.marcarReservasVencidas(LocalDate.now());
    }

    @Transactional
    public void crearReserva(Long disfrazId, ReservaRequest request, String correoUsuario) {
        actualizarReservasVencidas();

        validarFechas(request);

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Disfraz disfraz = disfrazRepository.buscarDetallePorId(disfrazId)
                .orElseThrow(() -> new IllegalArgumentException("Disfraz no encontrado"));

        boolean hayCruce = reservaRepository.existeCruceDeFechas(
                disfrazId,
                request.getFechaInicio(),
                request.getFechaFin()
        );

        if (hayCruce) {
            throw new IllegalArgumentException("Este disfraz ya está reservado en las fechas seleccionadas");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setDisfraz(disfraz);
        reserva.setFechaInicio(request.getFechaInicio());
        reserva.setFechaFin(request.getFechaFin());
        reserva.setTipo(request.getTipo());
        reserva.setEstado(EstadoReserva.ACTIVA);
        reserva.setObservaciones(request.getObservaciones());

        Reserva reservaGuardada = reservaRepository.save(reserva);
        emailService.enviarCorreoReservaAdmin(reservaGuardada);
    }

    @Transactional
    public List<ReservaView> listarReservasPorCorreo(String correoUsuario) {
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        actualizarReservasVencidas();

        return reservaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::convertirAReservaView)
                .toList();
    }

    @Transactional
    public List<ReservaAdminView> listarReservasAdmin() {
        actualizarReservasVencidas();

        return reservaRepository.findAllByOrderByFechaCreacionDesc()
                .stream()
                .map(this::convertirAReservaAdminView)
                .toList();
    }

    @Transactional
    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        if (reserva.getEstado() == EstadoReserva.VENCIDA || reserva.getEstado() == EstadoReserva.FINALIZADA) {
            throw new IllegalArgumentException("No se puede cancelar una reserva vencida o finalizada");
        }

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    @Transactional
    public void finalizarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("La reserva no existe"));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalArgumentException("No se puede finalizar una reserva cancelada");
        }

        reserva.setEstado(EstadoReserva.FINALIZADA);
        reservaRepository.save(reserva);
    }

    private ReservaView convertirAReservaView(Reserva reserva) {
        return new ReservaView(
                reserva.getId(),
                reserva.getDisfraz() != null ? reserva.getDisfraz().getNombre() : "Disfraz no disponible",
                obtenerImagenDisfraz(reserva),
                reserva.getTipo(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getEstado(),
                reserva.getObservaciones()
        );
    }

    private ReservaAdminView convertirAReservaAdminView(Reserva reserva) {
        Usuario usuario = reserva.getUsuario();

        String nombreCliente = "Cliente no disponible";
        String correoCliente = "Sin correo";
        String telefonoCliente = "Sin teléfono";

        if (usuario != null) {
            nombreCliente = usuario.getNombre();

            if (usuario.getApellido() != null && !usuario.getApellido().isBlank()) {
                nombreCliente += " " + usuario.getApellido();
            }

            correoCliente = usuario.getCorreo();
            telefonoCliente = usuario.getTelefono() != null ? usuario.getTelefono() : "Sin teléfono";
        }

        return new ReservaAdminView(
                reserva.getId(),
                nombreCliente,
                correoCliente,
                telefonoCliente,
                reserva.getDisfraz() != null ? reserva.getDisfraz().getNombre() : "Disfraz no disponible",
                obtenerImagenDisfraz(reserva),
                reserva.getTipo(),
                reserva.getFechaInicio(),
                reserva.getFechaFin(),
                reserva.getEstado(),
                reserva.getObservaciones()
        );
    }

    private String obtenerImagenDisfraz(Reserva reserva) {
        if (reserva.getDisfraz() == null ||
                reserva.getDisfraz().getImagenes() == null ||
                reserva.getDisfraz().getImagenes().isEmpty()) {
            return null;
        }

        return reserva.getDisfraz()
                .getImagenes()
                .stream()
                .filter(imagen -> Boolean.TRUE.equals(imagen.getPrincipal()))
                .findFirst()
                .orElse(reserva.getDisfraz().getImagenes().get(0))
                .getUrlImagen();
    }

    private void validarFechas(ReservaRequest request) {
        LocalDate hoy = LocalDate.now();

        if (request.getFechaInicio() == null || request.getFechaFin() == null) {
            throw new IllegalArgumentException("Debe seleccionar fecha de inicio y fecha de fin");
        }

        if (request.getFechaInicio().isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio no puede estar en el pasado");
        }

        if (request.getFechaFin().isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de fin no puede estar en el pasado");
        }

        if (request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    @Transactional
    public boolean estaDisponible(Long disfrazId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Debe seleccionar fecha de inicio y fecha de fin");
        }

        LocalDate hoy = LocalDate.now();

        if (fechaInicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio no puede estar en el pasado");
        }

        if (fechaFin.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de fin no puede estar en el pasado");
        }

        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        reservaRepository.marcarReservasVencidas(LocalDate.now());

        return !reservaRepository.existeCruceDeFechas(disfrazId, fechaInicio, fechaFin);
    }
}