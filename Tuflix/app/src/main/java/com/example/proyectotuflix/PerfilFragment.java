package com.example.proyectotuflix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.proyectotuflix.databinding.FragmentPerfilBinding;
import com.example.proyectotuflix.model.Post;
import com.example.proyectotuflix.model.Resenya;
import com.example.proyectotuflix.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.List;


public class PerfilFragment extends AppFragment {

    private FragmentPerfilBinding binding;

    static String FRAGMENT_ID = "PerfilFragment";
    String userid = "";
    User userViewing = new User();

    public PerfilFragment(String userid) {
        this.userid = userid;
    }

    public PerfilFragment() {
        this.userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPerfilBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            binding.buttonEditarperfil.setVisibility(View.VISIBLE);
            binding.followUserButton.setVisibility(View.GONE);
            binding.viewFollowersButton.setVisibility(View.VISIBLE);
            binding.textView14.setText("Mis publicaciones");
        } else {
            binding.buttonEditarperfil.setVisibility(View.GONE);
            binding.followUserButton.setVisibility(View.VISIBLE);
            binding.viewFollowersButton.setVisibility(View.GONE);
            binding.textView14.setText("Sus publicaciones");
        }

//        Get all the posts of the user:

        db.collection("posts").whereEqualTo("userid", userid).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Post post = documentSnapshot.toObject(Post.class);
                        posts.add(post);
                    }
                    binding.recyclerViewPosts.setAdapter(new PostsAdapter(posts, requireActivity(), PerfilFragment.this));
                    binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
                });



            db.collection("resenyas").whereEqualTo("userid", userid).get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<Resenya> resenyas = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Resenya resenya = documentSnapshot.toObject(Resenya.class);
                    resenyas.add(resenya);
                }
                binding.recyclerViewResenyas.setAdapter(new ResenyasAdapter(requireContext(), resenyas, PerfilFragment.this));
                binding.recyclerViewResenyas.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            });



        binding.buttonEditarperfil.setOnClickListener(v -> {
            navController.navigate(R.id.action_perfilFragment_to_ajustesPerfilFragment);
        });

        binding.todovideos.setOnClickListener(v -> {
            navController.navigate(R.id.action_perfilFragment_to_misVideosFragment);
        });


        binding.todoresenyas.setOnClickListener(v -> {
            navController.navigate(R.id.action_perfilFragment_to_resenyasUserFragment);
        });

        db.collection("users").document(userid).addSnapshotListener((document, e) -> {

            userViewing = document.toObject(User.class);
            binding.numFollowers.setText(userViewing.followersList.size() + "");

            Glide.with(requireContext()).load(userViewing.photo).circleCrop().into(binding.photo);
            binding.name.setText(userViewing.name);
            binding.email.setText(userViewing.correo);

            if(userid != FirebaseAuth.getInstance().getCurrentUser().getUid()) {
                if (userViewing.followersList != null) {
                    if (userViewing.followersList.containsKey(auth.getCurrentUser().getUid())) {
                        binding.followUserButton.setText("Unfollow");
                    } else {
                        binding.followUserButton.setText("Follow");
                    }
                }}
        });

        binding.followUserButton.setOnClickListener(view1 -> {
            if (binding.followUserButton.getText().toString().trim().toLowerCase().equals("seguir")) {
                db.collection("users").document(userid)
                        .update("followersList." + auth.getCurrentUser().getUid(), !userViewing.followersList.containsKey(auth.getUid()) ? true : FieldValue.delete());
                binding.numFollowers.setText(userViewing.followersList.size() + "");
                binding.followUserButton.setText("Unfollow");
            } else {
//                Si ya seguimos al usuaio, lo eliminamos de la lista de seguidores:
                db.collection("users").document(userid)
                        .update("followersList." + auth.getCurrentUser().getUid(), !userViewing.followersList.containsKey(auth.getUid()) ? false : FieldValue.delete());
                binding.numFollowers.setText(userViewing.followersList.size() + "");
                binding.followUserButton.setText("Seguir");
            }
        });

        if(userid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            binding.viewFollowersButton.setOnClickListener(v -> {
                navController.navigate(R.id.action_perfilFragment_to_profileListFragment);
            });
        }

        db.collection("posts").get().addOnSuccessListener(collection -> {
            List<Post> misPosts = new ArrayList<>();
            for(DocumentSnapshot document : collection.getDocuments()) {
                if(document.get("userid").equals(userid)) {
                    misPosts.add(document.toObject(Post.class));
                }
            }

            binding.recyclerViewPosts.setAdapter(new PostsAdapter(misPosts, requireContext(), PerfilFragment.this));
            binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        });
    }

}