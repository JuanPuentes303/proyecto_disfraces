package com.disfracesrivera.config;

import com.disfracesrivera.model.Categoria;
import com.disfracesrivera.model.Rol;
import com.disfracesrivera.repository.CategoriaRepository;
import com.disfracesrivera.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final CategoriaRepository categoriaRepository;

    public DataInitializer(RolRepository rolRepository, CategoriaRepository categoriaRepository) {
        this.rolRepository = rolRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(String... args) {
        crearRolSiNoExiste("ROLE_USER");
        crearRolSiNoExiste("ROLE_ADMIN");

        crearCategoriaSiNoExiste("Terror", "Disfraces de miedo, Halloween y personajes oscuros");
        crearCategoriaSiNoExiste("Infantil", "Disfraces para niños y niñas");
        crearCategoriaSiNoExiste("Superhéroes", "Disfraces de héroes y personajes de acción");
        crearCategoriaSiNoExiste("Princesas", "Disfraces de princesas y fantasía");
        crearCategoriaSiNoExiste("Animales", "Disfraces de animales");
        crearCategoriaSiNoExiste("Época", "Disfraces antiguos, clásicos o históricos");
    }

    private void crearRolSiNoExiste(String nombre) {
        if (rolRepository.findByNombre(nombre).isEmpty()) {
            rolRepository.save(new Rol(nombre));
        }
    }

    private void crearCategoriaSiNoExiste(String nombre, String descripcion) {
        if (categoriaRepository.findByNombre(nombre).isEmpty()) {
            categoriaRepository.save(new Categoria(nombre, descripcion));
        }
    }
}