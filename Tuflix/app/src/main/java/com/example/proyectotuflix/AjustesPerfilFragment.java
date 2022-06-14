package com.example.proyectotuflix;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.databinding.FragmentAjustesPerfilBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;
import java.util.UUID;


public class AjustesPerfilFragment extends AppFragment {

    private FragmentAjustesPerfilBinding binding;
    private Uri uriNuevaFoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentAjustesPerfilBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cambiarcontra.setOnClickListener(v -> {
            navController.navigate(R.id.action_ajustesPerfilFragment_to_cambiarContrasenyaFragment);
        });

        binding.photo.setOnClickListener(view2 -> {
            galeria.launch("image/*");
        });

        binding.photo2.setOnClickListener(view2 -> {
            galeria.launch("image/*");
        });

        appViewModel.uriImagenPerfilSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(this).load(uri).into(binding.photo);
            uriNuevaFoto = uri;
        });

        binding.actualiza.setOnClickListener(view1 -> {
            if (!binding.nameEditText.getText().toString().isEmpty()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.nameEditText.getText().toString())
                        .build();
                user.updateProfile(profileUpdates);
            }
            if (uriNuevaFoto != null) {
                FirebaseStorage.getInstance()
                        .getReference("/images/" + UUID.randomUUID() + ".jpg")
                        .putFile(uriNuevaFoto)
                        .continueWithTask(task1 -> Objects.requireNonNull(task1.getResult()).getStorage().getDownloadUrl())
                        .addOnSuccessListener(urlDescarga -> {
                            uriNuevaFoto = urlDescarga;
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uriNuevaFoto)
                                    .build();
                            user.updateProfile(profileUpdates);
                        });
            }
            navController.navigate(R.id.homeFragment);
            Log.d("asd", "User profile updated.");
        });


        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                Glide.with(requireContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl()).circleCrop().into(binding.photo);
                binding.nameEditText.setText(firebaseAuth.getCurrentUser().getDisplayName());
            }
        });

    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> appViewModel.setUriImagenPerfilSeleccionada(uri));

}
