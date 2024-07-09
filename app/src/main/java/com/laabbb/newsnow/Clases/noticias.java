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
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
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

public class noticias extends Fragment implements MenuProvider{

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

        // Añadir el MenuProvider al MenuHost
        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
    }

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menuinicio, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.btn_vizualizacion) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog, null);
            bottomSheetDialog.setContentView(bottomSheetView);
            // Configura los listeners para las opciones del BottomSheet
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
