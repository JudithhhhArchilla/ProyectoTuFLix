package com.example.proyectotuflix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectotuflix.databinding.FragmentEscribirResenyaBinding;
import com.example.proyectotuflix.model.Resenya;

public class EscribirResenyaFragment extends AppFragment {

    FragmentEscribirResenyaBinding binding;

    String id = "";

    String colleccion = "";

    public EscribirResenyaFragment(String id, String colleccion) {
        this.id = id;
        this.colleccion = colleccion;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentEscribirResenyaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.publicar.setOnClickListener(v -> {
            db.collection("resenyas").document(id+":"+auth.getCurrentUser().getUid()).set(new Resenya(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getEmail(), auth.getCurrentUser().getPhotoUrl().toString(), binding.contenidoASubir.getText().toString(), id)).addOnSuccessListener(success -> {
                navController.popBackStack();
                navController.popBackStack();
            });
        });
    }
}