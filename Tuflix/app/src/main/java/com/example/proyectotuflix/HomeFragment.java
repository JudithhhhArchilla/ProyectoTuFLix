package com.example.proyectotuflix;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectotuflix.databinding.FragmentHomeBinding;
import com.example.proyectotuflix.model.Pelicula;
import com.example.proyectotuflix.model.Post;
import com.example.proyectotuflix.model.Serie;
import com.google.firebase.firestore.DocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends AppFragment {

    ArrayList<Serie> series = new ArrayList<>();
    ArrayList<Pelicula> peliculas = new ArrayList<>();
    private FragmentHomeBinding binding;
    List<Serie> seriesAGuardar = new ArrayList<>();
    List<Pelicula> pelisAGuardar = new ArrayList<>();

    MutableLiveData<List<Serie>> seriesLiveData = new MutableLiveData<>();
    MutableLiveData<List<Pelicula>> pelisLiveData = new MutableLiveData<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentHomeBinding.inflate(inflater, container, false)).getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();

        binding.add.setOnClickListener(v -> {
            navController.navigate(R.id.action_homeFragment_to_subirVideoFragment);
        });

        binding.textViewSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.seriesFragment);
            }
        });

        binding.textViewPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.peliculaFragment);
            }
        });

        binding.textViewMisVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.misVideosFragment);
            }
        });

        if (auth.getCurrentUser() == null)  {
            navController.navigate(R.id.iniciarSesionFragment);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        seriesLiveData.setValue(new ArrayList<>());
        pelisLiveData.setValue(new ArrayList<>());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        String urlSeries = "https://api.themoviedb.org/3/tv/popular?api_key=fdb483b65456c03c7e3fe82254b80c44&language=en-US&page=1";
        String urlPelis = "https://api.themoviedb.org/3/movie/popular?api_key=fdb483b65456c03c7e3fe82254b80c44&language=en-US&page=1";


        JsonObjectRequest jsonObjectRequest = series(urlSeries);
        JsonObjectRequest jsonObjectRequest2 = pelis(urlPelis);

        requestQueue.add(jsonObjectRequest);
        requestQueue.add(jsonObjectRequest2);

        db.collection("posts").get().addOnSuccessListener(success -> {
            adaptarResultadosVideos(success.toObjects(Post.class));
        });

        createSeriesInFirebase();

        createFilmsInFirebase();

    }

    private List<Post> loadVideos() {
//        Get all the videos from firebase:

        return db.collection("posts").get().getResult().toObjects(Post.class);
    }

    private JsonObjectRequest pelis(String urlPelis) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlPelis,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Convert the response string into a JSON object:
                        try {
//                            Get the JSONArray of movies:
                            JSONArray jsonArray = response.getJSONArray("results");
//                            Loop through the array of movies:
                            for (int i = 0; i < jsonArray.length(); i++) {
//                                Get the current movie:
                                JSONObject movie = jsonArray.getJSONObject(i);
//                                Get the title, release date, and poster URL:
                                String title = movie.getString("original_title");
                                String urlImageTrailer = movie.getString("poster_path");
                                String overview = movie.getString("overview");
                                String fecha = movie.getString("release_date");
                                double votos = movie.getDouble("vote_average");
                                pelisAGuardar.add(new Pelicula(urlImageTrailer, title, overview, fecha, votos));
                            }
                            pelisLiveData.setValue(pelisAGuardar);
//                            Las series se guardan en el Firebase en la colección series y después se recuperan en el RecyclerView:
                            if(auth.getCurrentUser() != null) {
                                pelisLiveData.observe(getViewLifecycleOwner(), series -> {
                                    adaptarResultadosPelis(pelisLiveData.getValue());
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(requireContext(), "No se han podido obtener los ", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonObjectRequest;
    }

    @NonNull
    private JsonObjectRequest series(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url,
                null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
//                        Convert the response string into a JSON object:
                        try {
//                            Get the JSONArray of movies:
                            JSONArray jsonArray = response.getJSONArray("results");
//                            Loop through the array of movies:
                            for (int i = 0; i < jsonArray.length(); i++) {
//                                Get the current movie:
                                JSONObject movie = jsonArray.getJSONObject(i);
//                                Get the title, release date, and poster URL:
                                String title = movie.getString("original_name");
                                String urlImageTrailer = movie.getString("poster_path");
                                String overview = movie.getString("overview");
                                String fecha = movie.getString("first_air_date");
                                double votos = movie.getDouble("vote_average");
                                seriesAGuardar.add(new Serie(urlImageTrailer, title, overview, fecha, votos));
                            }
                            seriesLiveData.setValue(seriesAGuardar);
//                            Las series se guardan en el Firebase en la colección series y después se recuperan en el RecyclerView:

                            if(auth.getCurrentUser() != null) {
                                seriesLiveData.observe(getViewLifecycleOwner(), series -> {
                                    if(series != null) {
                                        adaptarResultadosSeries(seriesLiveData.getValue());
                                    }
                                });
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(requireContext(), "No se han podido obtener los ", Toast.LENGTH_SHORT).show();
                    }
                });
        return jsonObjectRequest;
    }

    private void adaptarResultadosSeries(List<Serie> series) {

        MyAdapter myAdapter = new MyAdapter(series, getContext(), HomeFragment.this);

        binding.recyclerSerie.setAdapter(myAdapter);
        binding.recyclerSerie.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void adaptarResultadosPelis(List<Pelicula> pelis) {

        AdapterPelis myAdapter = new AdapterPelis(pelis, getContext(), HomeFragment.this);

        binding.recyclerPelis.setAdapter(myAdapter);
        binding.recyclerPelis.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    private void adaptarResultadosVideos(List<Post> misVideos) {

        PostsAdapter myAdapter = new PostsAdapter(misVideos, requireContext(), HomeFragment.this);

        binding.recyclerMisVideos.setAdapter(myAdapter);
        binding.recyclerMisVideos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createSeriesInFirebase() {
//        Add the movies to the Firebase firestore:
        List<Serie> seriesAdevolver = new ArrayList<>();

        db.collection("series").get().addOnCompleteListener(success -> {
            if (success.getResult().isEmpty()) {
                for (Serie serie : seriesAGuardar) {

//            Solo hay que hacerlo la primera vez, sino se generarán duplicados:

                    String serieid = db.collection("series").document().getId();

                    serie.serieid = serieid;

                    db.collection("series").document(serieid).set(serie);
                    seriesAdevolver.add(serie);
                }
//                Esto gasta mucha cuota en firebase:
//            } else {
//                for (Serie serie : seriesAGuardar) {
//                    //Get the series collection and for each one of them, save the id:
//                    db.collection("series").get().addOnSuccessListener(success2 -> {
//                        for (DocumentSnapshot ds : success2.getDocuments()) {
//                            Serie serie2 = ds.toObject(Serie.class);
//                            serie2.serieid = ds.getId();
//                            db.collection("series").document(serie2.serieid).set(serie2);
//                            seriesAdevolver.add(serie);
//                        }
//                    });
//                }
            }
        });
    }

    private void createFilmsInFirebase() {
//        Add the movies to the Firebase firestore:
        List<Pelicula> pelisADevolver = new ArrayList<>();

        db.collection("pelis").get().addOnCompleteListener(success -> {
            if (success.getResult().isEmpty()) {
                for (Pelicula peli : pelisAGuardar) {

//            Solo hay que hacerlo la primera vez, sino se generarán duplicados:

                    String peliid = db.collection("pelis").document().getId();

                    peli.peliid = peliid;

                    db.collection("pelis").document(peliid).set(peli);
                    pelisADevolver.add(peli);
                }
//                Esto gasta mucha cuota en firebase:
//            } else {
//                for (Pelicula serie : pelisAGuardar) {
//                    Get the pelis collection and for each one of them, save the id:
//                    db.collection("pelis").get().addOnSuccessListener(success2 -> {
//                        for (DocumentSnapshot ds : success2.getDocuments()) {
//                            Pelicula peli2 = ds.toObject(Pelicula.class);
//                            peli2.peliid = ds.getId();
//                            db.collection("pelis").document(peli2.peliid).set(peli2);
//                            pelisADevolver.add(serie);
//                        }
//                    });
//                }
            }
        });
    }
}

