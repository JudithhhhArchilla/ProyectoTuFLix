package com.example.proyectotuflix;

import static com.example.proyectotuflix.ProfileListFragment.userid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.proyectotuflix.databinding.FragmentPostsBinding;
import com.example.proyectotuflix.model.Post;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends AppFragment {

    private FragmentPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentPostsBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("posts").get().addOnSuccessListener(collection -> {
            List<Post> misPosts = new ArrayList<>();
            for (DocumentSnapshot document : collection.getDocuments()) {
                if (document.get("userid").equals(userid)) {
                    misPosts.add(document.toObject(Post.class));
                }
            }
        });
//            db.collection("posts").get().addOnSuccessListener(success -> {
//                binding.recyclerPosts.setAdapter(
//                        new PostsAdapter(success.toObjects(Post.class), requireContext(), PostsFragment.this)
//                );
////                binding.recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//                binding.recyclerPosts.setLayoutManager(new GridLayoutManager(getContext(), 2));
//            });
//        }
    }
}