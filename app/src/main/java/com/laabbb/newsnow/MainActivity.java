package com.laabbb.newsnow;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laabbb.newsnow.Adaptadores.NoticiaAdaptador;
import com.laabbb.newsnow.Clases.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recNoticia;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    NoticiaAdaptador adaptador;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    String url = "https://p3.qr4me.net/";
    ArrayList<Noticia> noticias = new ArrayList<>();
    Map<Integer, Runnable> menuActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración de EdgeToEdge y ajustes de padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización del Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicialización del BottomNavigationView y configuración del listener
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inicialización del mapa de acciones
        menuActions = new HashMap<>();
        menuActions.put(R.id.btn_noticias, () -> Toast.makeText(MainActivity.this, "Noticias seleccionadas", Toast.LENGTH_SHORT).show());
        menuActions.put(R.id.btn_favoritos, () -> Toast.makeText(MainActivity.this, "Favoritos seleccionados", Toast.LENGTH_SHORT).show());
        menuActions.put(R.id.btn_usuario, () -> Toast.makeText(MainActivity.this, "Usuario seleccionado", Toast.LENGTH_SHORT).show());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = menuActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuinicio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_vizualizacion) {
            Toast.makeText(this, "Ver", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
