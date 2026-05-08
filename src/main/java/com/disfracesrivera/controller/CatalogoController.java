package com.disfracesrivera.controller;

import com.disfracesrivera.model.Disfraz;
import com.disfracesrivera.repository.CategoriaRepository;
import com.disfracesrivera.service.DisfrazService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CatalogoController {

    private final DisfrazService disfrazService;
    private final CategoriaRepository categoriaRepository;

    public CatalogoController(
            DisfrazService disfrazService,
            CategoriaRepository categoriaRepository
    ) {
        this.disfrazService = disfrazService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String talla,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model
    ) {
        List<Disfraz> resultados = disfrazService.buscarDisfraces(
                busqueda,
                categoriaId,
                talla,
                genero,
                precioMin,
                precioMax
        );

        List<Disfraz> aleatorios = disfrazService.obtenerAleatorios();

        model.addAttribute("disfraces", resultados);
        model.addAttribute("aleatorios", aleatorios);
        model.addAttribute("categorias", categoriaRepository.findAll());

        model.addAttribute("busqueda", busqueda);
        model.addAttribute("categoriaSeleccionada", categoriaId);
        model.addAttribute("tallaSeleccionada", talla);
        model.addAttribute("generoSeleccionado", genero);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);

        return "catalogo";
    }
}
