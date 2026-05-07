package com.disfracesrivera.repository;

import com.disfracesrivera.model.Disfraz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DisfrazRepository extends JpaRepository<Disfraz, Long> {

    List<Disfraz> findByActivoTrue();

    List<Disfraz> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Disfraz> findByCategoriaIdAndActivoTrue(Long categoriaId);

    @Query(value = "SELECT * FROM disfraces WHERE activo = true ORDER BY RAND() LIMIT 6", nativeQuery = true)
    List<Disfraz> obtenerSeisAleatorios();
}