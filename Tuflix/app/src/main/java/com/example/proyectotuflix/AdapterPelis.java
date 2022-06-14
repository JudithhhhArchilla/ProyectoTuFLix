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

import com.example.proyectotuflix.model.Pelicula;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPelis extends RecyclerView.Adapter<AdapterPelis.MyViewHolder> {

    private List<Pelicula> pelis;
    private Context mContext1;
    private Fragment currentFragment;

    public AdapterPelis(List<Pelicula> pelis, Context mContext1, Fragment currentFragment) {
        this.pelis = pelis;
        this.mContext1 = mContext1;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public AdapterPelis.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext1);
        View view = inflater.inflate(R.layout.viewholder_imagen, parent, false );
        return new AdapterPelis.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPelis.MyViewHolder holder, int position) {
        holder.imageView.setVisibility(View.VISIBLE);
        Picasso.get().load("https://image.tmdb.org/t/p/original" + pelis.get(holder.getAdapterPosition()).urlImagenPeli).fit()
                .centerCrop()
                .into(holder.imageView);

        holder.title.setText(pelis.get(holder.getAdapterPosition()).peliTitulo);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("pelis").get().addOnSuccessListener(success -> {
                    for(DocumentSnapshot ds : success.getDocuments()) {
                        if(ds.get("peliTitulo").toString().equalsIgnoreCase(pelis.get(holder.getAdapterPosition()).peliTitulo)) {
                            setFragment(new ClickVideoFragment(ds.getId(), "pelis", currentFragment));
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
        return pelis.size();
    }

    public void setData(List<Pelicula> pelis) {
        this.pelis = pelis;
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