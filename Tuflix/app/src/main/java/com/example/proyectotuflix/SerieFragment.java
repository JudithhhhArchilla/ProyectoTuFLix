package com.example.proyectotuflix;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.proyectotuflix.databinding.FragmentSerieBinding;
import com.example.proyectotuflix.model.Serie;

public class SerieFragment extends AppFragment {

    private FragmentSerieBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentSerieBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("series").get().addOnSuccessListener(success -> {
            binding.postsRecyclerView.setAdapter(
                    new MyAdapter(success.toObjects(Serie.class), requireContext(), SerieFragment.this)
            );
//            binding.postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.postsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        });
    }
}
