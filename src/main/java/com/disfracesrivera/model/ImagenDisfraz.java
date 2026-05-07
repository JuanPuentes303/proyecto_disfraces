package com.disfracesrivera.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_disfraz")
public class ImagenDisfraz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @Column(nullable = false)
    private Boolean principal = false;

    @ManyToOne
    @JoinColumn(name = "disfraz_id", nullable = false)
    private Disfraz disfraz;

    public ImagenDisfraz() {
    }

    public ImagenDisfraz(String urlImagen, Boolean principal, Disfraz disfraz) {
        this.urlImagen = urlImagen;
        this.principal = principal;
        this.disfraz = disfraz;
    }

    public Long getId() {
        return id;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public Disfraz getDisfraz() {
        return disfraz;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public void setDisfraz(Disfraz disfraz) {
        this.disfraz = disfraz;
    }
}