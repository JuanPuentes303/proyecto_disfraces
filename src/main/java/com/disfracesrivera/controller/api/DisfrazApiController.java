package com.disfracesrivera.controller.api;

import com.disfracesrivera.model.Disfraz;
import com.disfracesrivera.service.DisfrazService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disfraces")
public class DisfrazApiController {

    private final DisfrazService disfrazService;

    public DisfrazApiController(DisfrazService disfrazService) {
        this.disfrazService = disfrazService;
    }

    @GetMapping
    public List<Disfraz> listar() {
        return disfrazService.listarActivos();
    }
}