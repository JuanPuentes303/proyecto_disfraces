package com.disfracesrivera.controller;

import com.disfracesrivera.dto.DisfrazDetalleView;
import com.disfracesrivera.dto.ReservaRequest;
import com.disfracesrivera.model.TipoReserva;
import com.disfracesrivera.service.DisfrazService;
import com.disfracesrivera.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/disfraces")
public class DisfrazController {

    private final DisfrazService disfrazService;
    private final ReservaService reservaService;

    public DisfrazController(
            DisfrazService disfrazService,
            ReservaService reservaService
    ) {
        this.disfrazService = disfrazService;
        this.reservaService = reservaService;
    }

    @GetMapping("/{id}")
    public String detalleDisfraz(@PathVariable Long id, Model model) {
        DisfrazDetalleView disfraz = disfrazService.obtenerDetallePorId(id);

        prepararModeloDetalle(model, disfraz, new ReservaRequest());

        return "detalle-disfraz";
    }

    @PostMapping("/{id}/reservar")
    public String reservarDisfraz(
            @PathVariable Long id,
            @Valid @ModelAttribute("reservaRequest") ReservaRequest reservaRequest,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) {
        DisfrazDetalleView disfraz = disfrazService.obtenerDetallePorId(id);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            prepararModeloDetalle(model, disfraz, reservaRequest);
            return "detalle-disfraz";
        }

        try {
            reservaService.crearReserva(id, reservaRequest, authentication.getName());
            return "redirect:/usuario/reservas?creada";
        } catch (IllegalArgumentException e) {
            prepararModeloDetalle(model, disfraz, reservaRequest);
            model.addAttribute("errorReserva", e.getMessage());
            return "detalle-disfraz";
        }
    }

    private void prepararModeloDetalle(
            Model model,
            DisfrazDetalleView disfraz,
            ReservaRequest reservaRequest
    ) {
        model.addAttribute("disfraz", disfraz);
        model.addAttribute("reservaRequest", reservaRequest);
        model.addAttribute("tiposReserva", TipoReserva.values());
    }
}