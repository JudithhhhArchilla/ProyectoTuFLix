package com.example.proyectotuflix;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    public List<Post> posts = new ArrayList<>();
    public Context context;
    public boolean playingSomething;
    private Fragment currentFragment;

    public PostsAdapter(List<Post> posts, Context context , Fragment currentFragment) {
        this.posts = posts;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public PostsAdapter.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_imagen, parent, false);
        return new PostsAdapter.PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Post post = posts.get(position);

        if(post.mediaType.equals("video")) {
            holder.foto.setVisibility(View.GONE);
            holder.videoView2.setVisibility(View.VISIBLE);
            holder.videoView2.setVideoURI(Uri.parse(post.mediaUrl));
            holder.videoView2.setVideoPath(post.mediaUrl);
            if(!playingSomething) {
                holder.videoView2.start();
                playingSomething = true;
            }

            holder.videoView2.setOnClickListener(v ->{
                setFragment(new ClickVideoFragment(post.postid, "posts", currentFragment));
            });

        } else if(post.mediaType.equals("image")) {
            holder.videoView2.setVisibility(View.GONE);
            holder.foto.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(post.mediaUrl)
                    .centerCrop()
                    .into(holder.foto);
        } else {
            holder.videoView2.setVisibility(View.GONE);
            holder.foto.setVisibility(View.GONE);
        }

        holder.nombre.setText(post.title);
        holder.contenido.setText(post.description);
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
        return posts.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {
        public VideoView videoView2;
        private ImageView foto;
        private TextView nombre;
        private TextView contenido;
//        private ConstraintLayout row_layout;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.foto);
            videoView2 = itemView.findViewById(R.id.videoView2);
            nombre = itemView.findViewById(R.id.tituloHolder);
            contenido = itemView.findViewById(R.id.contenidoPost);
//            row_layout = itemView.findViewById(R.id.row_layout);
        }
    }
}

