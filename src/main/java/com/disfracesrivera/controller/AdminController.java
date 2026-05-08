package com.disfracesrivera.controller;

import com.disfracesrivera.dto.DisfrazRequest;
import com.disfracesrivera.service.DisfrazService;
import com.disfracesrivera.service.ReservaService;
import com.disfracesrivera.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DisfrazService disfrazService;
    private final ReservaService reservaService;
    private final CategoriaRepository categoriaRepository;

    public AdminController(
            DisfrazService disfrazService,
            ReservaService reservaService,
            CategoriaRepository categoriaRepository
    ) {
        this.disfrazService = disfrazService;
        this.reservaService = reservaService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/disfraces")
    public String listarDisfraces(Model model) {
        model.addAttribute("disfraces", disfrazService.listarActivos());
        return "admin/disfraces";
    }

    @GetMapping("/disfraces/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("disfrazRequest", new DisfrazRequest());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "admin/crear-disfraz";
    }

    @PostMapping("/disfraces/nuevo")
    public String guardarDisfraz(
            @Valid @ModelAttribute("disfrazRequest") DisfrazRequest disfrazRequest,
            BindingResult bindingResult,
            @RequestParam("imagen") MultipartFile imagen,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return "admin/crear-disfraz";
        }

        try {
            disfrazService.crearDisfraz(disfrazRequest, imagen);
            return "redirect:/admin/disfraces?creado";
        } catch (RuntimeException e) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("errorDisfraz", e.getMessage());
            return "admin/crear-disfraz";
        }
    }

    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        model.addAttribute("reservas", reservaService.listarReservasAdmin());
        return "admin/reservas";
    }

    @PostMapping("/reservas/{id}/cancelar")
    public String cancelarReserva(@PathVariable Long id) {
        reservaService.cancelarReserva(id);
        return "redirect:/admin/reservas?cancelada";
    }

    @PostMapping("/reservas/{id}/finalizar")
    public String finalizarReserva(@PathVariable Long id) {
        reservaService.finalizarReserva(id);
        return "redirect:/admin/reservas?finalizada";
    }
}