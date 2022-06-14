package com.example.proyectotuflix;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.proyectotuflix.model.Post;
import com.example.proyectotuflix.model.User;

public class AppViewModel extends AndroidViewModel {


    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();
    public MutableLiveData<Uri> uriImagenPerfilSeleccionada = new MutableLiveData<>();

    public static class Media {
        public Uri uri;
        public String tipo;

        public Media(Uri uri, String tipo) {
            this.uri = uri;
            this.tipo = tipo;
        }
    }

    public MutableLiveData<Post> postSeleccionado = new MutableLiveData<>();
    public MutableLiveData<Media> mediaSeleccionado = new MutableLiveData<>();

    public User userLoggedIn;

    public void setUriImagenSeleccionada(Uri uri) {
        uriImagenSeleccionada.setValue(uri);
    }

    public void setUriImagenPerfilSeleccionada(Uri uri) {
        uriImagenPerfilSeleccionada.setValue(uri);
    }

    public void setMediaSeleccionado(Uri uri, String type) {
        mediaSeleccionado.setValue(new Media(uri, type));
    }
}
