package com.example.proyectotuflix;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.databinding.FragmentSubirVideoBinding;
import com.example.proyectotuflix.model.Post;
import com.example.proyectotuflix.model.User;

import java.util.UUID;


public class SubirVideoFragment extends AppFragment {

    public Uri mediaUri;
    public String mediaTipo;
    MutableLiveData<String> name = new MutableLiveData<>("");
    String profilePic = "";

    private FragmentSubirVideoBinding binding;

    //
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentSubirVideoBinding.inflate(inflater, container, false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.videoGaleria.setOnClickListener(v -> seleccionarVideo());

        binding.publicar.setOnClickListener(v -> publicar());


        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            this.mediaUri = media.uri;
            this.mediaTipo = media.tipo;

            Glide.with(this).load(media.uri).into(binding.previsualizacion);
        });

    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new GetContent(), uri -> {
        appViewModel.setMediaSeleccionado(uri, mediaTipo);
    });

    private void publicar() {
        String title = binding.contenido.getText().toString();
        String description = binding.contenido2.getText().toString();

        if (title.isEmpty() && description.isEmpty()) {
            binding.contenido.setError("Required");
            binding.contenido2.setError("Required");
            return;
        }

        binding.publicar.setEnabled(false);

        if (mediaTipo == null) {
            writeNewPost(title, description, "");
        } else {
            uploadAndWriteNewPost(title, description);
        }
    }


    private void seleccionarVideo() {
        mediaTipo = "video";
        galeria.launch("video/*");
    }


    private void writeNewPost(String title, String description, @Nullable String mediaUrl) {

        if(auth.getCurrentUser().getPhotoUrl() == null) {
            profilePic = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__480.png";
        } else {
            profilePic = auth.getCurrentUser().getPhotoUrl().toString();
        }

        if(auth.getCurrentUser().getDisplayName() == null) {
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                name.postValue(documentSnapshot.toObject(User.class).name);
            });
        } else {
            name.postValue(auth.getCurrentUser().getDisplayName());
        }

        name.observe(getViewLifecycleOwner(), name -> {
            if(!name.equals("")) {
                String postid = db.collection("posts").document().getId();

                db.collection("posts").document(postid)
                        .set(new Post(postid, uid, name, profilePic, title, description, mediaUrl, mediaTipo))
                        .addOnSuccessListener(documentReference -> {
                            navController.popBackStack();
                            appViewModel.setMediaSeleccionado(null, null);
                            return;
                        });
            }
        });

    }

    private void uploadAndWriteNewPost(String title, final String description) {
        storage.getReference(mediaTipo + "/" + UUID.randomUUID())
                .putFile(mediaUri)
                .continueWithTask(task -> task.getResult().getStorage().getDownloadUrl())
                .addOnSuccessListener(url -> writeNewPost(title, description, url.toString()));
    }


}
