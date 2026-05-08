package com.disfracesrivera.dto;

import com.disfracesrivera.model.EstadoReserva;
import com.disfracesrivera.model.TipoReserva;

import java.time.LocalDate;

public class ReservaAdminView {

    private Long id;
    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;
    private String nombreDisfraz;
    private String imagenDisfrazUrl;
    private TipoReserva tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoReserva estado;
    private String observaciones;

    public ReservaAdminView(
            Long id,
            String nombreCliente,
            String correoCliente,
            String telefonoCliente,
            String nombreDisfraz,
            String imagenDisfrazUrl,
            TipoReserva tipo,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            EstadoReserva estado,
            String observaciones
    ) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.telefonoCliente = telefonoCliente;
        this.nombreDisfraz = nombreDisfraz;
        this.imagenDisfrazUrl = imagenDisfrazUrl;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getNombreDisfraz() {
        return nombreDisfraz;
    }

    public String getImagenDisfrazUrl() {
        return imagenDisfrazUrl;
    }

    public TipoReserva getTipo() {
        return tipo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}