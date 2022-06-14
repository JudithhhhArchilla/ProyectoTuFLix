package com.example.proyectotuflix;

import com.google.firebase.firestore.Query;

public class FavoritosFragment extends HomeFragment {

        Query setQuery(){
            return db.collection("posts").whereEqualTo("likes."+uid, true).limit(50);
        }

}