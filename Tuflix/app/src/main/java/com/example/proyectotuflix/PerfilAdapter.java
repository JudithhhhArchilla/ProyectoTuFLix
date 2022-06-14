package com.example.proyectotuflix;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.databinding.UserHolderBinding;
import com.example.proyectotuflix.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.ViewHolder> {

    private final NavController navController;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;

    List<User> userList = new ArrayList<>();
    Context context;
    String userSelectedID;

    public PerfilAdapter(List<User> userList, String userSelectedID, Context context, NavController navController, FirebaseAuth firebaseAuth, FirebaseFirestore db) {
        this.navController = navController;
        this.firebaseAuth = firebaseAuth;
        this.db = db;
        this.context = context;
        this.userSelectedID = userSelectedID;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PerfilAdapter.ViewHolder(UserHolderBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        Glide.with(context).load(user.photo).centerCrop().into(holder.binding.foto);
        holder.binding.name.setText("@" + user.name);
        holder.binding.correo.setText(user.correo);

        holder.binding.layoutProfile.setOnClickListener(view -> {
            navController.navigate(R.id.action_profileListFragment_to_perfilBuscadoFragment);
            userList.clear();
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UserHolderBinding binding;

        public ViewHolder(@NonNull UserHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}