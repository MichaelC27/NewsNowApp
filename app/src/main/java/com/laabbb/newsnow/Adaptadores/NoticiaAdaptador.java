package com.laabbb.newsnow.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.laabbb.newsnow.*;
import com.laabbb.newsnow.Clases.C_DB_Likes;
import com.laabbb.newsnow.Clases.C_DB_Noticias;
import com.laabbb.newsnow.Clases.Noticia;
import com.laabbb.newsnow.Clases.SessionManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

public class NoticiaAdaptador extends RecyclerView.Adapter<NoticiaAdaptador.ViewHolder> {
    Context context; // Contexto de la aplicación
    ArrayList<Noticia> lista; // Lista de noticias
    C_DB_Likes dbLikes; // Instancia de la base de datos de likes
    String username; // Usuario actual
    Set<String> likedIds; // Set para almacenar los IDs de noticias a las que se les ha dado "me gusta"

    // Constructor para inicializar el adaptador con contexto y lista de noticias
    public NoticiaAdaptador(Context context, ArrayList<Noticia> lista) {
        this.context = context;
        this.lista = lista;
        dbLikes = new C_DB_Likes((Activity) context);
        username = SessionManager.getInstance().getUsername();
        likedIds = dbLikes.getLikedNewsIds(username); // Cargar los IDs guardados en SQLite
    }

    @NonNull
    @Override
    public NoticiaAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de la vista de la noticia
        View view = LayoutInflater.from(context).inflate(R.layout.noticia_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaAdaptador.ViewHolder holder, int position) {
        // Asignar los datos de la noticia en la posición actual al ViewHolder
        holder.lblTitulo.setText(lista.get(position).getTitulo());
        holder.lblAutor.setText(lista.get(position).getAutor());
        Glide.with(context).load(lista.get(position).getImagen()).into(holder.imgImagen);

        // Obtener el estado actual de "me gusta" desde el modelo de Noticia
        boolean isLiked = lista.get(position).getMegusta();

        // Configurar la imagen según el estado actual de "me gusta" al cargar la vista
        if (isLiked || likedIds.contains(lista.get(position).getId())) {
            holder.imgMegusta.setImageResource(R.drawable.heart_final);
        } else {
            holder.imgMegusta.setImageResource(R.drawable.heart_inicial);
        }

        // Configurar el botón de "Me gusta"
        holder.imgMegusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Actualizar la imagen según el nuevo estado de "me gusta"
                if (likedIds.contains(lista.get(position).getId())) {
                    holder.imgMegusta.setImageResource(R.drawable.heart_inicial);
                    dbLikes.removeLike(username, lista.get(position).getId()); // Eliminar el ID de la base de datos
                    likedIds.remove(lista.get(position).getId()); // Actualizar el Set local
                    Toast.makeText(context, "Id No Favoritos: " + lista.get(position).getId(), Toast.LENGTH_SHORT).show();
                } else {
                    holder.imgMegusta.setImageResource(R.drawable.heart_final);
                    dbLikes.addLike(username, lista.get(position).getId()); // Agregar el ID a la base de datos
                    likedIds.add(lista.get(position).getId()); // Actualizar el Set local
                    Toast.makeText(context, "Id Favoritos: " + lista.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar el clic en la imagen de la noticia para abrir la actividad de detalles de la noticia
        holder.imgImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crear un intent para abrir la actividad de detalles de la noticia
                Intent intent = new Intent(context, InfoNoticias.class);
                intent.putExtra("id", lista.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Devolver el número total de elementos en la lista de noticias
        return lista.size();
    }

    // ViewHolder para contener las vistas de los elementos de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblTitulo, lblAutor; // Título y autor de la noticia
        ImageView imgImagen, imgMegusta; // Imagen de la noticia y botón de "me gusta"

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar vistas del diseño de la noticia
            lblTitulo = itemView.findViewById(R.id.lblTitulo);
            lblAutor = itemView.findViewById(R.id.lblAutor);
            imgImagen = itemView.findViewById(R.id.imgImagen);
            imgMegusta = itemView.findViewById(R.id.imgMegusta);
        }
    }
}
