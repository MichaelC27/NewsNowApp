package com.laabbb.newsnow.Clases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.laabbb.newsnow.Adaptadores.NoticiaAdaptador;
import com.laabbb.newsnow.R;
import com.laabbb.newsnow.Clases.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class noticias extends Fragment {

    private RecyclerView recNoticia;
    private RequestQueue requestQueue;
    private NoticiaAdaptador adaptador;
    private ArrayList<Noticia> noticias = new ArrayList<>();
    private String url = "https://p3.qr4me.net/";
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias, container, false);

        // Inicializar Toolbar
        toolbar = view.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        // Inicializar RecyclerView y adaptador
        recNoticia = view.findViewById(R.id.recNoticia);
        adaptador = new NoticiaAdaptador(getActivity(), noticias);
        recNoticia.setLayoutManager(new LinearLayoutManager(getActivity()));
        recNoticia.setAdapter(adaptador);

        // Obtener noticias del servidor
        fetchNoticias();

        // Habilitar opciones de menú
        setHasOptionsMenu(true);

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
                    recNoticia.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    bottomSheetDialog.dismiss();
                }
            });

           bottomSheetView.findViewById(R.id.btn_tabla).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recNoticia.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                noticias.add(new Noticia(
                        jsonObject.getString("id"),
                        jsonObject.getString("titulo"),
                        jsonObject.getString("autor"),
                        jsonObject.getString("detalle"),
                        false,
                        jsonObject.getString("imagen")
                ));
            }
            adaptador.notifyDataSetChanged(); // Notificar al adaptador de los cambios en los datos
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
