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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectotuflix.databinding.FragmentBuscarUsuariosBinding;
import com.example.proyectotuflix.model.User;

import java.util.ArrayList;
import java.util.List;

public class BuscarUsuariosFragment extends AppFragment {

    private FragmentBuscarUsuariosBinding binding;

    String buscar = "";

    List<User> users = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentBuscarUsuariosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.txtBuscarUsuario.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                users.clear();
                if(newText.length() > 0) {
                    buscar = newText;
                    db.collection("users").get().addOnCompleteListener(success -> {
                        for(User u : success.getResult().toObjects(User.class)) {
                            if(u.name.toLowerCase().contains(newText.toLowerCase())) {
                                users.add(u);
                            }
                        }

                        binding.usuariosEncontrados.setAdapter(new UserAdapter(users, requireContext(), BuscarUsuariosFragment.this));

                        binding.usuariosEncontrados.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }).addOnSuccessListener(success -> {

                    });
                    return true;
                }
                return false;
            }
        });

    }



}