package com.laabbb.newsnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.laabbb.newsnow.Clases.C_DB_Noticias;
import com.laabbb.newsnow.Clases.SessionManager;

public class Login extends AppCompatActivity {
    Button btn_login;
    EditText txt_user, txt_password;
    TextView lbl_registrar;
    C_DB_Noticias dbNoticias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar EdgeToEdge para manejar los insets de la pantalla
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_login);

        // Aplicar insets a la vista principal para manejar barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar instancia de la base de datos de noticias
        dbNoticias = new C_DB_Noticias(this);

        // Referenciar elementos de la interfaz de usuario
        btn_login = findViewById(R.id.btn_login);
        txt_user = findViewById(R.id.txt_user);
        txt_password = findViewById(R.id.txt_password);
        lbl_registrar = findViewById(R.id.lbl_registrar);

        // Configurar evento click para el texto de registro
        lbl_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir la actividad de registro al hacer clic en "Registrar"
                Intent intent = new Intent(Login.this, Registrar.class);
                startActivity(intent);
            }
        });

        // Configurar evento click para el botón de inicio de sesión
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener usuario y contraseña ingresados
                String usuario = txt_user.getText().toString();
                String password = txt_password.getText().toString();

                // Verificar credenciales en la base de datos
                if (dbNoticias.verificarUsuario(usuario, password)) {
                    // Mostrar mensaje de inicio de sesión exitoso
                    Toast.makeText(Login.this, "Inicio de sesión exitoso"+usuario, Toast.LENGTH_SHORT).show();

                    // Establecer el usuario actual en la sesión
                    SessionManager.getInstance().setUsername(usuario);

                    // Redirigir a la actividad principal
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finalizar la actividad actual
                } else {
                    // Mostrar mensaje de credenciales incorrectas
                    Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
