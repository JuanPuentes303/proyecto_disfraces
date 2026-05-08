package com.disfracesrivera.controller.api;

import com.disfracesrivera.dto.DisfrazAdminView;
import com.disfracesrivera.dto.DisfrazRequest;
import com.disfracesrivera.service.DisfrazService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/disfraces")
public class AdminDisfrazApiController {

    private final DisfrazService disfrazService;

    public AdminDisfrazApiController(DisfrazService disfrazService) {
        this.disfrazService = disfrazService;
    }

    @GetMapping
    public ResponseEntity<List<DisfrazAdminView>> listar() {
        return ResponseEntity.ok(disfrazService.listarTodosAdmin());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> crear(
            @Valid @ModelAttribute DisfrazRequest disfrazRequest,
            @RequestParam("imagen") MultipartFile imagen
    ) {
        disfrazService.crearDisfraz(disfrazRequest, imagen);

        return ResponseEntity.ok(
                Map.of("mensaje", "Disfraz creado correctamente")
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute DisfrazRequest disfrazRequest,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) {
        disfrazService.actualizarDisfraz(id, disfrazRequest, imagen);

        return ResponseEntity.ok(
                Map.of("mensaje", "Disfraz actualizado correctamente")
        );
    }

    @PostMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, String>> desactivar(@PathVariable Long id) {
        disfrazService.desactivarDisfraz(id);

        return ResponseEntity.ok(
                Map.of("mensaje", "Disfraz desactivado correctamente")
        );
    }
}