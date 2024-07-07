package com.laabbb.newsnow;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.laabbb.newsnow.Adaptadores.NoticiaAdaptador;
import com.laabbb.newsnow.Clases.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recNoticia;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    NoticiaAdaptador adaptador;
    String url = "https://p3.qr4me.net/";
    ArrayList<Noticia> noticias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar RecyclerView y adaptador
        initializeRecyclerView();

        // Obtener noticias del servidor
        fetchNoticias();
    }

    // Método para inicializar el RecyclerView y su adaptador
    private void initializeRecyclerView() {
        recNoticia = findViewById(R.id.recNoticia);
        adaptador = new NoticiaAdaptador(this, noticias);
        recNoticia.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        // Si se quiere usar un layout diferente, descomentar una de las líneas siguientes
        // recNoticia.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // recNoticia.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recNoticia.setAdapter(adaptador);
    }

    // Método para obtener noticias desde el servidor
    private void fetchNoticias() {
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseNoticias(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Manejar el error de la petición aquí
            }
        });
        requestQueue.add(stringRequest);
    }

    // Método para parsear el JSON de las noticias
    private void parseNoticias(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                noticias.add(new Noticia(
                        jsonObject.getString("id"),
                        jsonObject.getString("titulo"),
                        jsonObject.getString("autor"),
                        "",
                        0,
                        jsonObject.getString("imagen"),
                        jsonObject.getString("icono")
                ));
            }
            adaptador.notifyDataSetChanged(); // Notificar al adaptador de los cambios en los datos
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
