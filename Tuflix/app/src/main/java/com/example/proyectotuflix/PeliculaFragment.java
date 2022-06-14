package com.example.proyectotuflix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.proyectotuflix.databinding.FragmentPeliculaBinding;
import com.example.proyectotuflix.model.Pelicula;


public class PeliculaFragment extends AppFragment {

    private FragmentPeliculaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentPeliculaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("pelis").get().addOnSuccessListener(success -> {
            binding.pelisRecyclerView.setAdapter(
                    new AdapterPelis(success.toObjects(Pelicula.class), requireContext(), PeliculaFragment.this)
            );
//            binding.pelisRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.pelisRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        });
    }
}