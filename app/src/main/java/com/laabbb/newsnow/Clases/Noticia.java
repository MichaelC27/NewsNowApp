package com.laabbb.newsnow.Clases;


public class Noticia {
    private String id;
    private String titulo;
    private String autor;
    private String detalles;
    private int megusta;
    private String imagen;
    private String icono;

    public Noticia() {
    }

    public Noticia(String id, String titulo, String autor, String detalles, int megusta, String imagen, String icono) {
        this.id  = id;
        this.titulo = titulo;
        this.autor = autor;
        this.detalles = detalles;
        this.megusta = megusta;
        this.imagen = imagen;
        this.icono = icono;
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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public int getMegusta() {
        return megusta;
    }

    public void setMegusta(int megusta) {
        this.megusta = megusta;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
