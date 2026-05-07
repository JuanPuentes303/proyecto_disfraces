package com.disfracesrivera.repository;

import com.disfracesrivera.model.EstadoReserva;
import com.disfracesrivera.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByDisfrazId(Long disfrazId);

    List<Reserva> findByEstado(EstadoReserva estado);

    @Query("""
            SELECT COUNT(r) > 0
            FROM Reserva r
            WHERE r.disfraz.id = :disfrazId
            AND r.estado = 'ACTIVA'
            AND r.fechaInicio <= :fechaFin
            AND r.fechaFin >= :fechaInicio
            """)
    boolean existeCruceDeFechas(Long disfrazId, LocalDate fechaInicio, LocalDate fechaFin);

    @Modifying
    @Query("""
            UPDATE Reserva r
            SET r.estado = 'VENCIDA'
            WHERE r.estado = 'ACTIVA'
            AND r.fechaFin < :fechaActual
            """)
    void marcarReservasVencidas(LocalDate fechaActual);
}