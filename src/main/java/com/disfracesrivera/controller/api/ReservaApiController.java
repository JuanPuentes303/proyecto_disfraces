package com.disfracesrivera.controller.api;

import com.disfracesrivera.dto.ReservaRequest;
import com.disfracesrivera.dto.ReservaView;
import com.disfracesrivera.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {

    private final ReservaService reservaService;

    public ReservaApiController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaView>> misReservas(Authentication authentication) {
        List<ReservaView> reservas = reservaService.listarReservasPorCorreo(authentication.getName());
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/disfraz/{disfrazId}")
    public ResponseEntity<Map<String, String>> crearReserva(
            @PathVariable Long disfrazId,
            @Valid @RequestBody ReservaRequest reservaRequest,
            Authentication authentication
    ) {
        reservaService.crearReserva(disfrazId, reservaRequest, authentication.getName());

        return ResponseEntity.ok(
                Map.of("mensaje", "Reserva creada correctamente")
        );
    }

    @GetMapping("/disfraz/{disfrazId}/disponibilidad")
    public ResponseEntity<Map<String, Object>> consultarDisponibilidad(
            @PathVariable Long disfrazId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        boolean disponible = reservaService.estaDisponible(
                disfrazId,
                java.time.LocalDate.parse(fechaInicio),
                java.time.LocalDate.parse(fechaFin)
        );

        return ResponseEntity.ok(
                Map.of(
                        "disfrazId", disfrazId,
                        "fechaInicio", fechaInicio,
                        "fechaFin", fechaFin,
                        "disponible", disponible
                )
        );
    }
}