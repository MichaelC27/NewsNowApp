package com.laabbb.newsnow;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laabbb.newsnow.Clases.noticias;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Runnable> menuActions;

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


        // Inicialización del BottomNavigationView y configuración del listener
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inicialización del mapa de acciones
        menuActions = new HashMap<>();
        menuActions.put(R.id.btn_noticias, () -> loadFragment(new noticias()));
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

        // Cargar fragmento inicial
        if (savedInstanceState == null) {
            loadFragment(new noticias());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
