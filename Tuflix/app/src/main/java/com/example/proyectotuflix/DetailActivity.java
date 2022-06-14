package com.example.proyectotuflix;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyectotuflix.model.Pelicula;

public class DetailActivity extends AppCompatActivity {

    private NavController navController;
//    private DetailActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Como se linkea el layout con el activity???

//        binding = DetailActivityBinding.inflate(getLayoutInflater());
//        setContentView((binding = DetailActivityBinding.inflate(getLayoutInflater())).getRoot());

//        ni idea de lo de arriba



        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();

//        Get an intent variable from the MainActivity called peli:

        Bundle bundle = getIntent().getExtras();

        Pelicula peli = bundle.getParcelable("peli");

//        TODO: crear layout DetailActivity y linkearlo:

//        Picasso.get().load(peli.urlImagenPeli).into(binding.imageView);

//        Bind the peli object to the layout:

//        nombre.setText(peli.peliTitulo);

//        descripcion.setText(peli.descripcion);

//        fecha.setText(peli.fecha);

//        votos.setText(peli.votos);





    }

}
