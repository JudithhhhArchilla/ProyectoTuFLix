package com.example.proyectotuflix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectotuflix.databinding.FragmentProfileListBinding;
import com.example.proyectotuflix.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileListFragment extends AppFragment {

    private FragmentProfileListBinding binding;

    static String FRAGMENT_ID = "ProfileListFragment";

    ArrayList<String> followersList = new ArrayList<>();
    List<User> users= new ArrayList<>();
    User userToSendAdapter = new User();


    public static String userid = "";

    public ProfileListFragment() {
        this.userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public ProfileListFragment(String userid){
        this.userid = userid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentProfileListBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seguidores(userid);
    }

    private void adaptUser(List<User> list, RecyclerView recyclerView) {
        recyclerView.setAdapter(new UserAdapter( list, requireActivity(),ProfileListFragment.this));
        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
    }

    private void seguidores (String userid) {
        users.clear();
        followersList.clear();
        db.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            followersList = new ArrayList<String>(documentSnapshot.toObject(User.class).followersList.keySet());
            for (String useridtemp : followersList) {
                db.collection("users").document(useridtemp).get().addOnSuccessListener(documentSnapshot1 -> {
                    users.add(documentSnapshot1.toObject(User.class));
                    adaptUser(users, binding.recyclerFollowers);
                });
            }
        });
    }
}