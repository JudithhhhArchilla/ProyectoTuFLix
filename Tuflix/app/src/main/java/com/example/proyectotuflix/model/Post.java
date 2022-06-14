package com.example.proyectotuflix.model;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String userid;
    public String postid;
    public String author;
    public String authorPhotoUrl;
    public String title;
    public String description;
    public String mediaUrl;
    public String mediaType;
    public Map<String, Boolean> likes = new HashMap<>();

    public Post() {}

    public Post(String postid, String uid, String author, String authorPhotoUrl, String title, String description, String mediaUrl, String mediaType) {
        this.postid = postid;
        this.userid = uid;
        this.author = author;
        this.authorPhotoUrl = authorPhotoUrl;
        this.title = title;
        this.description = description;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }
}
