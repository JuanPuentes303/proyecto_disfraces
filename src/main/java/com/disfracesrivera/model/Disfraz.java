package com.disfracesrivera.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disfraces")
public class Disfraz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 50)
    private String talla;

    @Column(length = 50)
    private String genero;

    @Column(name = "precio_alquiler", precision = 10, scale = 2)
    private BigDecimal precioAlquiler;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Column(name = "disponible_venta", nullable = false)
    private Boolean disponibleVenta = true;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "disfraz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenDisfraz> imagenes = new ArrayList<>();

    @OneToMany(mappedBy = "disfraz")
    private List<Reserva> reservas = new ArrayList<>();

    public Disfraz() {
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

    public Boolean getActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<ImagenDisfraz> getImagenes() {
        return imagenes;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setDisponibleVenta(Boolean disponibleVenta) {
        this.disponibleVenta = disponibleVenta;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setImagenes(List<ImagenDisfraz> imagenes) {
        this.imagenes = imagenes;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}