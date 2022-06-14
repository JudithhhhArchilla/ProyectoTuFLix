package com.example.proyectotuflix;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.databinding.FragmentRegisterBinding;
import com.example.proyectotuflix.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;
import java.util.UUID;

public class RegistroFragment extends AppFragment {

    private FragmentRegisterBinding binding;
    private Uri uriImagen;
    private Uri downloadUriImagen;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.verifyEmailButton.setOnClickListener(v -> {
//        });

        binding.previsualizacion.setOnClickListener(v -> galeria.launch("image/*"));


        appViewModel.uriImagenPerfilSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if(!uri.equals(null) || !uri.equals("")) {
                Glide.with(this).load(uri).into(binding.previsualizacion);
                uriImagen = uri;
            }
        });

        binding.createAccountButton.setOnClickListener(v -> {

            if (binding.nameEditText.getText().toString().isEmpty()) {
                binding.nameEditText.setError("Se require un nombre de usuario.");
                return;
            }
            if (binding.emailEditText.getText().toString().isEmpty()) {
                binding.emailEditText.setError("Se requiere un correo electrónico.");
                return;
            }
            if (binding.passwordEditText.getText().toString().isEmpty()) {
                binding.passwordEditText.setError("Se requiere una contraseña.");
                return;
            }
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                            binding.emailEditText.getText().toString().trim(),
                            binding.passwordEditText.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (uriImagen != null) {
                                FirebaseStorage.getInstance().getReference("/images/" + UUID.randomUUID() + ".jpg")
                                        .putFile(uriImagen)
                                        .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                        .addOnSuccessListener(imageUrl -> saveUser((auth.getCurrentUser()).getUid(), binding.nameEditText.getText().toString().trim(), binding.emailEditText.getText().toString().trim(), binding.passwordEditText.getText().toString().trim(), imageUrl));
                            } else {
                                saveUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), binding.nameEditText.getText().toString().trim(), binding.emailEditText.getText().toString().trim(), binding.passwordEditText.getText().toString().trim(), null);
                            }
                            Toast.makeText(requireContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_registerFragment_to_homeFragment);

                        } else {
                            Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void saveUser(String uid, String usernameValue, String emailValue, String passwordValue, Uri uriImagen) {
        String imageUrl;

        if (uriImagen == null || uriImagen.toString().equals("")) {
            imageUrl = "";
        } else {
            imageUrl = uriImagen.toString();
        }

        User user = new User(uid, imageUrl, usernameValue, emailValue);

        db.collection("users")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .set(user);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Objects.requireNonNull(usernameValue))
                .setPhotoUri(Uri.parse(imageUrl))
                .build();
        auth.getCurrentUser().updateProfile(profileUpdates);
    }



    private final ActivityResultLauncher<String> galeria = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> appViewModel.setUriImagenPerfilSeleccionada(uri));
}