package com.example.proyectotuflix;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectotuflix.databinding.FragmentRecuperarContrasenyaBinding;
import com.example.proyectotuflix.databinding.FragmentRegisterBinding;
import com.google.android.material.textfield.TextInputEditText;

public class RecuperarContrasenyaFragment extends AppFragment {

    private FragmentRecuperarContrasenyaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentRecuperarContrasenyaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            binding.enviarContra.setOnClickListener(v -> {
            if (auth.getCurrentUser() != null) {
                if (auth.getCurrentUser().getEmail().equals(binding.correo.getText().toString().trim())) {
                    auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail());
                    navController.navigate(R.id.iniciarSesionFragment);
                }
            } else {
                auth.sendPasswordResetEmail(binding.correo.getText().toString().trim());
                navController.popBackStack();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.navigate(R.id.iniciarSesionFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }


}
