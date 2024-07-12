package com.laabbb.newsnow.Clases;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashSet;
import java.util.Set;

public class C_DB_Likes {
    private String username;
    private String news_id;
    private NewsHelperSQLite admin;
    private SQLiteDatabase db;
    private String BaseDatos = "NewsNow.db";

    public C_DB_Likes() {
    }

    public C_DB_Likes(Activity activity2) {
        admin = new NewsHelperSQLite(activity2, BaseDatos, null, 1);
        db = admin.getWritableDatabase();
    }

    public C_DB_Likes(String username, String news_id) {
        this.username = username;
        this.news_id = news_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    // Método para agregar un like a la base de datos
    public void addLike(String username, String newsId) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("news_id", newsId);
        db.insert("likes", null, values);
    }

    // Método para eliminar un like de la base de datos
    public void removeLike(String username, String newsId) {
        db.delete("likes", "username=? AND news_id=?", new String[]{username, newsId});
    }

    // Método para verificar si un artículo tiene like por el usuario
    public boolean isLiked(String username, String newsId) {
        Cursor cursor = db.query("likes", null, "username=? AND news_id=?", new String[]{username, newsId}, null, null, null);
        boolean liked = cursor.moveToFirst();
        cursor.close();
        return liked;
    }

    // Método para obtener todos los IDs de noticias que le gustan al usuario
    public Set<String> getLikedNewsIds(String username) {
        Set<String> likedIds = new HashSet<>();
        Cursor cursor = db.query("likes", new String[]{"news_id"}, "username=?", new String[]{username}, null, null, null);
        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex("news_id");
            if (columnIndex != -1) {
                likedIds.add(cursor.getString(columnIndex));
            }
        }
        cursor.close();
        return likedIds;
    }

}
