package com.example.proyectotuflix;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotuflix.model.Serie;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Serie> series;
    private Context mContext1;
    private Fragment currentFragment;

    public MyAdapter(List<Serie> trailers, Context mContext1, Fragment currentFragment) {
        this.series = trailers;
        this.mContext1 = mContext1;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext1);
        View view = inflater.inflate(R.layout.viewholder_imagen, parent, false );
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        Picasso.get().load("https://image.tmdb.org/t/p/original" + series.get(holder.getAdapterPosition()).urlImagenSerie).fit()
                .centerCrop()
                .into(holder.imageView);

        holder.title.setText(series.get(holder.getAdapterPosition()).serieTitulo);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("series").get().addOnSuccessListener(success -> {
                   for(DocumentSnapshot ds : success.getDocuments()) {
                       if(ds.get("serieTitulo").toString().equalsIgnoreCase(series.get(holder.getAdapterPosition()).serieTitulo)) {
                           setFragment(new ClickVideoFragment(ds.getId(), "series", currentFragment));
                       }
                   }
                });

            }
        });
    }

    private void setFragment(Fragment fragment) {
        currentFragment
            .getFragmentManager()
            .beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(HomeFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    public void setData(List<Serie> series) {
        this.series = series;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView title;

        private ConstraintLayout row_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.foto);
            title = itemView.findViewById(R.id.tituloHolder);
            row_layout = itemView.findViewById(R.id.row_layout);
        }
    }
}