package com.example.proyectotuflix.model;

import java.util.HashMap;

public class User {
    public String userid;
    public String photo;
    public String name;
    public String correo;
    public int followers;

    public HashMap<String, Boolean> followersList = new HashMap<>();

    public User() {
    }

    public User(String userid, String photo, String name, String correo) {
        this.userid = userid;
        this.photo = photo;
        this.name = name;
        this.correo = correo;
        this.followersList = new HashMap<>();
    }
}