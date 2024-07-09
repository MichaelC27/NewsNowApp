package com.laabbb.newsnow.Clases;


public class Noticia {
    private String id;
    private String titulo;
    private String autor;
    private String detalle;
    private boolean megusta;
    private String imagen;

    public Noticia() {
    }

    public Noticia(String id, String titulo, String autor, String detalle, boolean megusta, String imagen) {
        this.id  = id;
        this.titulo = titulo;
        this.autor = autor;
        this.detalle = detalle;
        this.megusta = megusta;
        this.imagen = imagen;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalles) {
        this.detalle = detalles;
    }

    public boolean getMegusta() {
        return megusta;
    }

    public void setMegusta(boolean megusta) {
        this.megusta = megusta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
