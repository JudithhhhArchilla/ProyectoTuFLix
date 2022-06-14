package com.example.proyectotuflix;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotuflix.databinding.FragmentClickVideoBinding;
import com.example.proyectotuflix.model.Pelicula;
import com.example.proyectotuflix.model.Post;
import com.example.proyectotuflix.model.Resenya;
import com.example.proyectotuflix.model.Serie;
import com.squareup.picasso.Picasso;


public class ClickVideoFragment extends AppFragment {

    String id = "";
    String tipo = "";
    private Serie serie;
    private Pelicula pelicula;
    private Post post;
    private Fragment currentFragment;

    public ClickVideoFragment(String id, String tipo, Fragment currentFragment) {
        this.id = id;
        this.tipo = tipo;
        this.currentFragment = currentFragment;
    }


    private FragmentClickVideoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentClickVideoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection(tipo).document(id).get().addOnSuccessListener(documentSnapshot -> {
            if(tipo.equals("series")) {
                serie = documentSnapshot.toObject(Serie.class);
                binding.linearAutor.setVisibility(View.GONE);
                binding.likesLinear.setVisibility(View.GONE);
                binding.contenido.setText(serie.serieTitulo);
                binding.contenido2.setText(serie.descripcion);
                Picasso.get().load("https://image.tmdb.org/t/p/original" + serie.urlImagenSerie).into(binding.imagen);
            }
            else if(tipo.equals("pelis")) {
                pelicula = documentSnapshot.toObject(Pelicula.class);
                binding.linearAutor.setVisibility(View.GONE);
                binding.likesLinear.setVisibility(View.GONE);
                binding.contenido.setText(pelicula.peliTitulo);
                binding.contenido2.setText(pelicula.descripcion);

                Picasso.get().load("https://image.tmdb.org/t/p/original" + pelicula.urlImagenPeli).into(binding.imagen);
            }
            else if(tipo.equals("posts")) {
                post = documentSnapshot.toObject(Post.class);

                binding.linearAutor.setVisibility(View.VISIBLE);
                if(post.authorPhotoUrl != "") {
                    Picasso.get().load(post.authorPhotoUrl).into(binding.authorfoto);
                }
                binding.autor.setText(post.author);
                binding.contenido.setText(post.title);
                binding.contenido2.setText(post.description);

//                Get the likes from firebase:

                db.collection("posts").document(post.postid).get().addOnSuccessListener(success -> {
                    if(success.toObject(Post.class).likes != null) {
                        binding.likesLinear.setVisibility(View.VISIBLE);
                        binding.numeroLikes.setText(success.toObject(Post.class).likes.size() + "");
                    }
                });

                if(post.mediaType.equals("video")) {
                    binding.imageViewTF4.setVisibility(View.GONE);
                    binding.videoView.setVisibility(View.VISIBLE);
                    binding.videoView.setVideoURI(Uri.parse(post.mediaUrl));
                    binding.videoView.start();
                }
                else {
                    binding.imageViewTF4.setVisibility(View.VISIBLE);
                    binding.videoView.setVisibility(View.GONE);
                    Picasso.get().load("https://image.tmdb.org/t/p/original" + post.mediaUrl).into(binding.imagen);
                }
            }


            binding.escribirResenya.setOnClickListener(v ->{
                setFragment(new EscribirResenyaFragment(id, tipo));
            });


        });

        db.collection("resenyas").whereEqualTo("idcorrespondiente", id).get().addOnSuccessListener(querySnapshot -> {
            if(querySnapshot.size() > 0) {
                binding.recyclerviewResenyas.setAdapter(new ResenyasAdapter(getContext(), querySnapshot.toObjects(Resenya.class), currentFragment));
                binding.recyclerviewResenyas.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                binding.recyclerviewResenyas.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerviewResenyas.setVisibility(View.GONE);
            }
        });




    }

    private void setFragment(Fragment fragment) {
        currentFragment
                .getFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(ClickVideoFragment.class.getSimpleName())
            .commit();
}

}