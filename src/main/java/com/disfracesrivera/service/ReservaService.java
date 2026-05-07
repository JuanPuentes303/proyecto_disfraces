package com.disfracesrivera.service;

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

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            DisfrazRepository disfrazRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.disfrazRepository = disfrazRepository;
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

        Disfraz disfraz = disfrazRepository.findById(disfrazId)
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

        reservaRepository.save(reserva);
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
}