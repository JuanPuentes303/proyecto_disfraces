package com.disfracesrivera.dto;

import java.math.BigDecimal;

public class DisfrazPublicView {

    private Long id;
    private String nombre;
    private String descripcion;
    private String talla;
    private String genero;
    private BigDecimal precioAlquiler;
    private BigDecimal precioCompra;
    private Boolean disponibleVenta;
    private String nombreCategoria;
    private String imagenPrincipalUrl;

    public DisfrazPublicView(
            Long id,
            String nombre,
            String descripcion,
            String talla,
            String genero,
            BigDecimal precioAlquiler,
            BigDecimal precioCompra,
            Boolean disponibleVenta,
            String nombreCategoria,
            String imagenPrincipalUrl
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.talla = talla;
        this.genero = genero;
        this.precioAlquiler = precioAlquiler;
        this.precioCompra = precioCompra;
        this.disponibleVenta = disponibleVenta;
        this.nombreCategoria = nombreCategoria;
        this.imagenPrincipalUrl = imagenPrincipalUrl;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTalla() {
        return talla;
    }

    public String getGenero() {
        return genero;
    }

    public BigDecimal getPrecioAlquiler() {
        return precioAlquiler;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public Boolean getDisponibleVenta() {
        return disponibleVenta;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public String getImagenPrincipalUrl() {
        return imagenPrincipalUrl;
    }
}