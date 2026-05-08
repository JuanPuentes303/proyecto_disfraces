package com.disfracesrivera.repository;

import com.disfracesrivera.model.Disfraz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DisfrazRepository extends JpaRepository<Disfraz, Long> {

    List<Disfraz> findByActivoTrue();

    @Query("""
            SELECT d
            FROM Disfraz d
            WHERE d.activo = true
            AND (:busqueda IS NULL OR :busqueda = '' OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
            AND (:categoriaId IS NULL OR d.categoria.id = :categoriaId)
            AND (:talla IS NULL OR :talla = '' OR d.talla = :talla)
            AND (:genero IS NULL OR :genero = '' OR d.genero = :genero)
            AND (:precioMin IS NULL OR d.precioAlquiler >= :precioMin)
            AND (:precioMax IS NULL OR d.precioAlquiler <= :precioMax)
            """)
    List<Disfraz> buscarConFiltros(
            String busqueda,
            Long categoriaId,
            String talla,
            String genero,
            BigDecimal precioMin,
            BigDecimal precioMax
    );

    @Query("""
            SELECT DISTINCT d
            FROM Disfraz d
            LEFT JOIN FETCH d.imagenes
            LEFT JOIN FETCH d.categoria
            WHERE d.activo = true
            AND (:busqueda IS NULL OR :busqueda = '' OR LOWER(d.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')))
            AND (:categoriaId IS NULL OR d.categoria.id = :categoriaId)
            AND (:talla IS NULL OR :talla = '' OR d.talla = :talla)
            AND (:genero IS NULL OR :genero = '' OR d.genero = :genero)
            AND (:precioMin IS NULL OR d.precioAlquiler >= :precioMin)
            AND (:precioMax IS NULL OR d.precioAlquiler <= :precioMax)
            """)
    List<Disfraz> buscarConFiltrosDetalle(
            String busqueda,
            Long categoriaId,
            String talla,
            String genero,
            BigDecimal precioMin,
            BigDecimal precioMax
    );

    @Query("""
            SELECT DISTINCT d
            FROM Disfraz d
            LEFT JOIN FETCH d.imagenes
            LEFT JOIN FETCH d.categoria
            WHERE d.id = :id
            """)
    Optional<Disfraz> buscarDetallePorId(Long id);

    @Query("""
            SELECT DISTINCT d
            FROM Disfraz d
            LEFT JOIN FETCH d.imagenes
            LEFT JOIN FETCH d.categoria
            ORDER BY d.fechaCreacion DESC
            """)
    List<Disfraz> listarTodosConDetalle();

    @Query(value = "SELECT * FROM disfraces WHERE activo = true ORDER BY RAND() LIMIT 6", nativeQuery = true)
    List<Disfraz> obtenerSeisAleatorios();
}