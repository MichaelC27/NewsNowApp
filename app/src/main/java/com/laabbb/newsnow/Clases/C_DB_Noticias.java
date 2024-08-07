package com.laabbb.newsnow.Clases;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class C_DB_Noticias {
    private String usuario;
    private String password;
    private String nombre;
    private String apellido;
    private String email;
    private NewsHelperSQLite admin;
    private SQLiteDatabase db;
    private String BaseDatos = "NewsNow.db";

    // Constructor vacío
    public C_DB_Noticias() {
    }

    // Constructor que inicializa la base de datos usando la actividad proporcionada
    public C_DB_Noticias(Activity activity2) {
        admin = new NewsHelperSQLite(activity2, BaseDatos, null, 1);
        db = admin.getWritableDatabase();
    }

    // Constructor que inicializa con datos de usuario
    public C_DB_Noticias(String usuario, String password, String nombre, String apellido, String email) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    // Getters y setters para los campos de usuario
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Método para insertar un nuevo usuario en la base de datos
    public boolean insertar() {
        ContentValues registro = new ContentValues();
        registro.put("username", this.usuario);
        registro.put("password", this.password);
        registro.put("first_name", this.nombre);
        registro.put("last_name", this.apellido);
        registro.put("email", this.email);
        long result = db.insert("users", null, registro);
        db.close();
        return result != -1; // Retorna true si la inserción es exitosa
    }

    // Método para verificar si un usuario ya existe en la base de datos
    public boolean verificarUsuarioExistente(String usuario) {
        SQLiteDatabase db = admin.getReadableDatabase();
        String[] projection = { "username" };
        String selection = "username = ?";
        String[] selectionArgs = { usuario };

        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean existeUsuario = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existeUsuario;
    }

    // Método para verificar la autenticación de un usuario basado en usuario y contraseña
    public boolean verificarUsuario(String usuario, String password) {
        SQLiteDatabase db = admin.getReadableDatabase();
        String[] projection = {
                "username",
                "password"
        };
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { usuario, password };

        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean existeUsuario = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existeUsuario;
    }

    // Método para seleccionar y devolver los datos de un usuario basado en el nombre de usuario
    public C_DB_Noticias seleccionarDatosUsuario(String username) {
        SQLiteDatabase db = admin.getReadableDatabase();
        String[] projection = {
                "username",
                "first_name",
                "last_name",
                "email"
        };
        String selection = "username = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query(
                "users",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        C_DB_Noticias usuario = new C_DB_Noticias(); // Crear instancia por defecto
        if (cursor.moveToFirst()) {
            usuario.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            usuario.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
            usuario.setApellido(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }

        cursor.close();
        db.close();
        return usuario;
    }
}
