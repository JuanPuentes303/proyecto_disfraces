package com.disfracesrivera.dto;

import com.disfracesrivera.model.TipoReserva;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ReservaRequest {

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede estar en el pasado")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @FutureOrPresent(message = "La fecha de fin no puede estar en el pasado")
    private LocalDate fechaFin;

    @NotNull(message = "Debe seleccionar si desea alquilar o comprar")
    private TipoReserva tipo;

    @Size(max = 500, message = "Las observaciones no pueden superar los 500 caracteres")
    private String observaciones;

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public TipoReserva getTipo() {
        return tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setTipo(TipoReserva tipo) {
        this.tipo = tipo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}