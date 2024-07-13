package com.laabbb.newsnow.Clases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.laabbb.newsnow.Adaptadores.LikesAdaptador;
import com.laabbb.newsnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class favoritos extends Fragment {
    private TextView lbl_favoritos;
    private RecyclerView recFavoritos;
    private RequestQueue requestQueue;
    private LikesAdaptador adaptador;
    private ArrayList<Noticia> favoritos = new ArrayList<>();
    private String url = "https://p3.qr4me.net/";
    private Toolbar toolbar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public favoritos() {
        // Required empty public constructor
    }

    // Método estático para crear una nueva instancia del fragmento con parámetros
    public static favoritos newInstance(String param1, String param2) {
        favoritos fragment = new favoritos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        // Inicializar Toolbar
        toolbar = view.findViewById(R.id.toolbar3);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        lbl_favoritos = view.findViewById(R.id.lbl_favoritos);
        // Inicializar RecyclerView y adaptador
        recFavoritos = view.findViewById(R.id.recFavoritos);
        adaptador = new LikesAdaptador(getActivity(), favoritos);
        recFavoritos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recFavoritos.setAdapter(adaptador);

        // Habilitar opciones de menú
        setHasOptionsMenu(true);
        // Obtener noticias del servidor
        fetchNoticias();

        return view;
    }

    // Inflar el menú de opciones en la barra de herramientas
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menuinicio, menu);
    }

    // Manejar eventos de selección de elementos del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_vizualizacion) {
            // Mostrar un dialogo de hoja inferior para cambiar la visualización del RecyclerView
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            // Configurar los listeners para las opciones del BottomSheet
            bottomSheetView.findViewById(R.id.btn_lista).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recFavoritos.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetView.findViewById(R.id.btn_tabla).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recFavoritos.setLayoutManager(new LinearLayoutManager(getActivity()));
                    bottomSheetDialog.dismiss();
                }
            });

            bottomSheetDialog.show();
            return true;
        }
        return false;
    }

    // Método para obtener noticias del servidor usando Volley
    private void fetchNoticias() {
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseNoticias(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "Error al obtener noticias", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    // Método para parsear el JSON de las noticias recibidas del servidor
    private void parseNoticias(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            ArrayList<Noticia> todasLasNoticias = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                todasLasNoticias.add(new Noticia(
                        jsonObject.getString("id"),
                        jsonObject.getString("titulo"),
                        jsonObject.getString("autor"),
                        jsonObject.getString("detalle"),
                        false,
                        jsonObject.getString("imagen")
                ));
            }
            // Filtrar noticias favoritas
            filtrarFavoritos(todasLasNoticias);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para filtrar las noticias favoritas
    private void filtrarFavoritos(ArrayList<Noticia> todasLasNoticias) {
        // Obtener el nombre de usuario de SessionManager
        String username = SessionManager.getInstance().getUsername();

        // Obtener los IDs de noticias favoritas desde la base de datos
        C_DB_Likes dbLikes = new C_DB_Likes(getActivity());
        Set<String> likedIds = dbLikes.getLikedNewsIds(username);

        // Filtrar las noticias que están en la lista de favoritos
        ArrayList<Noticia> noticiasFavoritas = new ArrayList<>();
        for (Noticia noticia : todasLasNoticias) {
            if (likedIds.contains(noticia.getId())) {
                noticiasFavoritas.add(noticia);
            }
        }

        // Actualizar el adaptador con las noticias favoritas
        favoritos.clear();
        favoritos.addAll(noticiasFavoritas);
        adaptador.notifyDataSetChanged();
    }
}
