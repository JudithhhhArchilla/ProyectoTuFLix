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

import com.example.proyectotuflix.model.Resenya;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResenyasAdapter extends RecyclerView.Adapter<ResenyasAdapter.ResenyaHolder> {

    public Context context;
    public List<Resenya> resenyas;
    public Fragment currentFragment;

    public ResenyasAdapter(Context context, List<Resenya> resenyas, Fragment currentFragment) {
        this.context = context;
        this.resenyas = resenyas;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public ResenyaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_valoracion_user, parent, false );
        return new ResenyaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenyaHolder holder, int position) {

        Picasso.get().load(resenyas.get(position).userImage).into(holder.fotoValoracion);
        holder.autorValoracion.setText(resenyas.get(position).username);
        holder.correoValoracion.setText(resenyas.get(position).correo);
        holder.comentarioValoracion.setText(resenyas.get(position).valoracion);
    }

    @Override
    public int getItemCount() {
        return resenyas.size();
    }

    public class ResenyaHolder extends RecyclerView.ViewHolder {

        public ImageView fotoValoracion;
        public TextView autorValoracion;
        public TextView correoValoracion;
        public TextView comentarioValoracion;

        public ResenyaHolder(@NonNull View itemView) {
            super(itemView);
            fotoValoracion = itemView.findViewById(R.id.fotoValoracion);
            autorValoracion = itemView.findViewById(R.id.autorValoracion);
            correoValoracion = itemView.findViewById(R.id.correoValoracion);
            comentarioValoracion = itemView.findViewById(R.id.comentarioValoracion);
        }
    }
}
