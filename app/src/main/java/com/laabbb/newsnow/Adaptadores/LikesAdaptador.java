package com.laabbb.newsnow.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laabbb.newsnow.Clases.C_DB_Likes;
import com.laabbb.newsnow.Clases.Noticia;
import com.laabbb.newsnow.Clases.SessionManager;
import com.laabbb.newsnow.InfoNoticias;
import com.laabbb.newsnow.R;

import java.util.ArrayList;
import java.util.Set;

public class LikesAdaptador extends RecyclerView.Adapter<LikesAdaptador.ViewHolder> {
    Context context;
    ArrayList<Noticia> lista;
    Set<String> likedIds;
    C_DB_Likes dbLikes; // Instancia de la base de datos de likes
    String username; // Usuario actual

    public LikesAdaptador(Context context, ArrayList<Noticia> lista) {
        this.context = context;
        this.lista = lista;
        dbLikes = new C_DB_Likes((Activity) context);
        username = SessionManager.getInstance().getUsername();
        likedIds = dbLikes.getLikedNewsIds(username); // Cargar los IDs guardados en SQLite
    }

    @NonNull
    @Override
    public LikesAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noticia_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LikesAdaptador.ViewHolder holder, int position) {
        // Asignar los datos de la noticia en la posición actual al ViewHolder
        holder.lblTitulo.setText(lista.get(position).getTitulo());
        holder.lblAutor.setText(lista.get(position).getAutor());
        Boolean bt_like = lista.get(position).getMegusta();
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

                } else {
                    holder.imgMegusta.setImageResource(R.drawable.heart_final);
                    dbLikes.addLike(username, lista.get(position).getId()); // Agregar el ID a la base de datos
                    likedIds.add(lista.get(position).getId()); // Actualizar el Set local

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
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView lblTitulo, lblAutor;
        ImageView imgImagen, imgMegusta;

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
