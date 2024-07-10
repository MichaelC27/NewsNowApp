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
import com.laabbb.newsnow.Clases.favoritos;
import com.laabbb.newsnow.Clases.noticias;
import com.laabbb.newsnow.Clases.usuario;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Map<Integer, Runnable> menuActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configuración de EdgeToEdge y ajustes de padding para manejar barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización del BottomNavigationView y configuración del listener para los ítems
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Inicialización del mapa de acciones para asociar ítems del menú con fragmentos
        menuActions = new HashMap<>();
        menuActions.put(R.id.btn_noticias, () -> loadFragment(new noticias()));
        menuActions.put(R.id.btn_favoritos, () -> loadFragment(new favoritos()));
        menuActions.put(R.id.btn_usuario, () -> loadFragment(new usuario()));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Obtener la acción asociada al ítem seleccionado y cargar el fragmento correspondiente
            Runnable action = menuActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true; // Indicar que se ha manejado la selección del ítem
            }
            return false; // Indicar que no se ha manejado la selección del ítem
        });

        // Cargar el fragmento inicial al iniciar la actividad
        if (savedInstanceState == null) {
            loadFragment(new noticias()); // Fragmento inicial: noticias
        }
    }

    // Método para cargar un fragmento en el contenedor principal
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Reemplazar el contenido del contenedor con el fragmento dado
        transaction.addToBackStack(null); // Agregar la transacción al back stack para permitir navegación hacia atrás
        transaction.commit(); // Confirmar la transacción
    }

}
