package com.laabbb.newsnow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.laabbb.newsnow.Clases.C_DB_Noticias;

public class Registrar extends AppCompatActivity {
    Button btn_registrar;
    EditText txtUsuario, txtPassword, txtNombre, txtApellido, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilitar EdgeToEdge para manejar barras de sistema
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de componentes de la interfaz de usuario
        txtUsuario = findViewById(R.id.txt_user);
        txtPassword = findViewById(R.id.txt_password);
        txtNombre = findViewById(R.id.txt_nombre);
        txtApellido = findViewById(R.id.txt_apellido);
        txtEmail = findViewById(R.id.txt_email);
        btn_registrar = findViewById(R.id.btn_registrar);

        // Configuración del OnClickListener para el botón de registro
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {
                    // Verificar si el usuario ya existe antes de insertarlo
                    if (usuarioNoExiste(txtUsuario.getText().toString())) {
                        // Si el usuario no existe, proceder con el registro
                        registrarUsuario();
                    } else {
                        // Si el usuario ya existe, mostrar mensaje de error
                        Toast.makeText(Registrar.this, "El nombre de usuario ya está en uso", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Método para validar que todos los campos estén completos
    private boolean validarCampos() {
        if (txtUsuario.getText().toString().isEmpty() ||
                txtPassword.getText().toString().isEmpty() ||
                txtNombre.getText().toString().isEmpty() ||
                txtApellido.getText().toString().isEmpty() ||
                txtEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para verificar si el usuario ya está registrado en la base de datos
    private boolean usuarioNoExiste(String usuario) {
        C_DB_Noticias db = new C_DB_Noticias(Registrar.this);
        return !db.verificarUsuarioExistente(usuario);
    }

    // Método para registrar un nuevo usuario en la base de datos
    private void registrarUsuario() {
        C_DB_Noticias obj = new C_DB_Noticias(Registrar.this);
        obj.setUsuario(txtUsuario.getText().toString());
        obj.setPassword(txtPassword.getText().toString());
        obj.setNombre(txtNombre.getText().toString());
        obj.setApellido(txtApellido.getText().toString());
        obj.setEmail(txtEmail.getText().toString());

        boolean isInserted = obj.insertar();

        if (isInserted) {
            Toast.makeText(Registrar.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
            abrirLogin(); // Llama al método para abrir Login después del registro exitoso
        } else {
            Toast.makeText(Registrar.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para abrir la actividad de login después del registro exitoso
    private void abrirLogin() {
        Intent intent = new Intent(Registrar.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpiar la pila de actividades
        startActivity(intent);
        finish(); // Finalizar la actividad actual para evitar volver atrás con el botón de retroceso
    }
}
