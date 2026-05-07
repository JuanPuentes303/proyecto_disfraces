package com.disfracesrivera.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class DisfrazRequest {

    @NotBlank(message = "El nombre del disfraz es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    private String nombre;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "La talla es obligatoria")
    private String talla;

    @NotBlank(message = "El género es obligatorio")
    private String genero;

    @NotNull(message = "El precio de alquiler es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de alquiler debe ser mayor a 0")
    private BigDecimal precioAlquiler;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;

    @NotNull(message = "Debe seleccionar una categoría")
    private Long categoriaId;

    private Boolean disponibleVenta = true;

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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public Boolean getDisponibleVenta() {
        return disponibleVenta;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setPrecioAlquiler(BigDecimal precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public void setDisponibleVenta(Boolean disponibleVenta) {
        this.disponibleVenta = disponibleVenta;
    }
}