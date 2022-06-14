package com.example.proyectotuflix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectotuflix.databinding.FragmentIniciarSesionBinding;
import com.example.proyectotuflix.model.User;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;


public class IniciarSesionFragment extends AppFragment {

    private FragmentIniciarSesionBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentIniciarSesionBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signInProgressBar.setVisibility(View.GONE);

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());

        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(requireContext()));

        binding.googleSignIn.setOnClickListener(view1 -> {
            signInClient.launch(googleSignInClient.getSignInIntent());
        });

        binding.btnIniciarSessio.setOnClickListener(v -> {
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                            binding.emailEditText.getText().toString(),
                            binding.passwordEditText.getText().toString()
                    ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    navController.popBackStack();
                } else {
                    Toast.makeText(requireContext(), task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.textBtnRecoveryPasswd.setOnClickListener(v -> {
            navController.navigate(R.id.action_iniciarSesionFragment_to_recuperarcontraseñaFragment);
        });

        binding.registrarse.setOnClickListener(v -> {
            navController.navigate(R.id.action_iniciarSesionFragment_to_registerFragment);
        });

    }

    ActivityResultLauncher<Intent> signInClient = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class));
                    } catch (ApiException e) {

                    }
                }
            });

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if (account == null) return;

        binding.signInProgressBar.setVisibility(View.VISIBLE);

        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        navController.popBackStack();
                    } else {
                        binding.signInProgressBar.setVisibility(View.GONE);
                    }
                });
    }



}