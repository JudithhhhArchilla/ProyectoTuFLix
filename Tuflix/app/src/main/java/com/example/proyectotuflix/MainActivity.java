package com.example.proyectotuflix;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;

import com.example.proyectotuflix.databinding.ActivityMainBinding;
import com.example.proyectotuflix.databinding.NavHeaderMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavHeaderMainBinding navHeaderMainBinding;

    private NavigationView navView;
    private BottomNavigationView bottom_nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));
        FirebaseFirestore.getInstance().setFirestoreSettings(new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build());

        setSupportActionBar(binding.toolbar);

        navView = findViewById(R.id.nav_view);
        bottom_nav_view = findViewById(R.id.bottom_nav_view);
        navView.setItemIconTintList(null);
        bottom_nav_view.setItemIconTintList(null);


        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();


        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId() == R.id.iniciarSesionFragment || destination.getId() == R.id.recuperarcontasenyaFragment || destination.getId() == R.id.registerFragment) {
                binding.toolbar.setVisibility(View.GONE);
                binding.bottomNavView.setVisibility(View.GONE);
            } else {
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.bottomNavView.setVisibility(View.VISIBLE);
            }
        });

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                if(firebaseAuth.getCurrentUser().getPhotoUrl() != null) {
                    Glide.with(getApplicationContext()).load(firebaseAuth.getCurrentUser().getPhotoUrl()).circleCrop().into(navHeaderMainBinding.photo);
                }
                navHeaderMainBinding.name.setText(firebaseAuth.getCurrentUser().getDisplayName());
                navHeaderMainBinding.email.setText(firebaseAuth.getCurrentUser().getEmail());
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}


