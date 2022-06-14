package com.example.proyectotuflix.model;

public class Resenya {
    public String userid;
    public String username;
    public String correo;
    public String userImage;
    public String valoracion;
    public String idcorrespondiente;

    public Resenya() {
    }

    public Resenya(String userid, String username, String correo, String userImage, String valoracion, String idcorrespondiente) {
        this.userid = userid;
        this.username = username;
        this.correo = correo;
        this.userImage = userImage;
        this.valoracion = valoracion;
        this.idcorrespondiente = idcorrespondiente;
    }
}
