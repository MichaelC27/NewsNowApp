package com.laabbb.newsnow.Clases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NewsHelperSQLite extends SQLiteOpenHelper {
    public NewsHelperSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (" +
                "username TEXT PRIMARY KEY, " +
                "password TEXT, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "email TEXT)");

        // Crear tabla de favoritos
        sqLiteDatabase.execSQL("CREATE TABLE likes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "news_id TEXT, " +
                "FOREIGN KEY(username) REFERENCES users(username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
