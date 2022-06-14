package com.example.proyectotuflix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    List<User> users;
    public Fragment currentFragment;

    public UserAdapter(List<User> users, Context context, Fragment currentFragment) {
        this.users = users;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        if (users.get(position).photo == null) {
            Glide.with(context)
                    .load(R.drawable.usuario)
                    .centerCrop()
                    .into(holder.foto);
        } else {
            Glide.with(context)
                    .load(users.get(position).photo)
                    .centerCrop()
                    .into(holder.foto);
        }


        holder.name.setText(users.get(position).name);

        holder.correo.setText(users.get(position).correo);


        holder.foto.setOnClickListener(view -> {
            PerfilFragment profileFragment = new PerfilFragment(users.get(position).userid);
            setFragment(profileFragment);
        });
    }

    private void setFragment(Fragment fragment) {
//
        if(currentFragment instanceof BuscarUsuariosFragment) {
            currentFragment
                    .getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(HomeFragment.class.getSimpleName())
                    .commit();
        }
        else {
            currentFragment
                    .getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(HomeFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView name;
        TextView correo;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            name = itemView.findViewById(R.id.name);
            correo = itemView.findViewById(R.id.correo);
        }
    }
}