package com.example.proyectotuflix.model;

import java.io.Serializable;

public class Pelicula implements Serializable {
    public String peliid;
    public String urlImagenPeli;
    public String peliTitulo;
    public String descripcion;
    public String fecha;
    public double votos;

    public Pelicula() {
    }

    public Pelicula(String urlImageTrailer, String titulo, String descripcion, String fecha, double votos) {
        this.urlImagenPeli = urlImageTrailer;
        this.peliTitulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.votos = votos;
    }
}