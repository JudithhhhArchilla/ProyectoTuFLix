package com.example.proyectotuflix;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.proyectotuflix.databinding.FragmentBuscadorBinding;
import com.example.proyectotuflix.model.Serie;

import java.util.ArrayList;
import java.util.List;

public class BuscadorFragment extends AppFragment {

    private FragmentBuscadorBinding binding;

    String buscar = "";

    List<Serie> series = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentBuscadorBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                series.clear();
                if(newText.length() > 0) {
                    buscar = newText;

                    db.collection("series").get().addOnSuccessListener(success -> {
                        for(Serie s : success.toObjects(Serie.class)) {
                            if(s.serieTitulo.toLowerCase().contains(newText.toLowerCase())) {
                                series.add(new Serie(s.urlImagenSerie, s.serieTitulo, s.descripcion, s.fecha, s.votos));
                            }
                        }
                    });

//                  Cast peliculas to list:
                    binding.encontrados.setAdapter(new MyAdapter(series, requireContext(), BuscadorFragment.this));
                    binding.encontrados.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                    return true;
                }
                return false;
            }
        });

    }

}