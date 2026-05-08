package com.disfracesrivera.controller.api;

import com.disfracesrivera.dto.DisfrazPublicView;
import com.disfracesrivera.service.DisfrazService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/disfraces")
public class DisfrazApiController {

    private final DisfrazService disfrazService;

    public DisfrazApiController(DisfrazService disfrazService) {
        this.disfrazService = disfrazService;
    }

    @GetMapping
    public ResponseEntity<List<DisfrazPublicView>> listar(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String talla,
            @RequestParam(required = false) String genero,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax
    ) {
        return ResponseEntity.ok(
                disfrazService.listarPublicoConFiltros(
                        busqueda,
                        categoriaId,
                        talla,
                        genero,
                        precioMin,
                        precioMax
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisfrazPublicView> detalle(@PathVariable Long id) {
        return ResponseEntity.ok(disfrazService.obtenerPublicoPorId(id));
    }
}