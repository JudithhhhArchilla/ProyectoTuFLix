package com.example.proyectotuflix.model;


import java.io.Serializable;

public class Serie implements Serializable {
    public String serieid;
    public String urlImagenSerie;
    public String serieTitulo;
    public String descripcion;
    public String fecha;
    public double votos;

    public Serie() {}

    public Serie(String urlImageTrailer, String titulo, String descripcion, String fecha, double votos) {
        this.urlImagenSerie = urlImageTrailer;
        this.serieTitulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.votos = votos;
    }
}
