package com.laabbb.newsnow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.laabbb.newsnow.Clases.Noticia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoNoticias extends AppCompatActivity {
    private TextView lblTitulo2;
    private RequestQueue requestQueue;
    private ArrayList<Noticia> noticias = new ArrayList<>();
    private String url = "https://p3.qr4me.net/";
    private TextView lbl_info_titulo, lbl_detalles;
    private ImageView img_noticia;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_noticias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar componentes
        toolbar = findViewById(R.id.toolbar_Info_N);
        setSupportActionBar(toolbar);
        img_noticia = findViewById(R.id.img_noticia);
        lbl_info_titulo = findViewById(R.id.lbl_info_titulo);
        lbl_detalles = findViewById(R.id.lbl_detalles);
        fetchNoticias();
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void fetchNoticias() {
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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

    private void parseNoticias(String response) {
        String idFiltro = getIntent().getExtras().getString("id");
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String noticiaId = jsonObject.getString("id");
                if (noticiaId.equals(idFiltro)) {
                    Noticia noticia = new Noticia(
                            noticiaId,
                            jsonObject.getString("titulo"),
                            jsonObject.getString("autor"),
                            jsonObject.getString("detalle"),
                            false,
                            jsonObject.getString("imagen")

                    );
                    noticias.add(noticia);

                    // Asignar detalles a los elementos UI
                    lbl_info_titulo.setText(noticia.getTitulo());
                    lbl_detalles.setText(noticia.getDetalle());
                    // Cargar imagen usando Glide (u otra biblioteca de tu elección)
                    Glide.with(this).load(noticia.getImagen()).into(img_noticia);
                    break; // Salir del bucle una vez que se encuentra la noticia
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_noticias, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        if (item.getItemId() == R.id.btn_compartir) {
            Toast.makeText(this, "Compartir", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
