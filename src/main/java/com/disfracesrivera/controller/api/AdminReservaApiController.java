package com.disfracesrivera.controller.api;

import com.disfracesrivera.dto.ReservaAdminView;
import com.disfracesrivera.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reservas")
public class AdminReservaApiController {

    private final ReservaService reservaService;

    public AdminReservaApiController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaAdminView>> listarReservas() {
        List<ReservaAdminView> reservas = reservaService.listarReservasAdmin();
        return ResponseEntity.ok(reservas);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Map<String, String>> cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);

        return ResponseEntity.ok(
                Map.of("mensaje", "Reserva cancelada correctamente")
        );
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Map<String, String>> finalizarReserva(@PathVariable Long id) {
        reservaService.finalizarReserva(id);

        return ResponseEntity.ok(
                Map.of("mensaje", "Reserva finalizada correctamente")
        );
    }
}