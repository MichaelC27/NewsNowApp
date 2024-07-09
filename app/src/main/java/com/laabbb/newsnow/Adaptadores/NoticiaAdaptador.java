package com.laabbb.newsnow.Adaptadores;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.laabbb.newsnow.*;
import com.laabbb.newsnow.Clases.Noticia;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticiaAdaptador extends RecyclerView.Adapter<NoticiaAdaptador.ViewHolder> {
    Context context;
    ArrayList<Noticia> lista;

    public NoticiaAdaptador(Context context, ArrayList<Noticia> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public NoticiaAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.noticia_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaAdaptador.ViewHolder holder, int position) {
        holder.lblTitulo.setText( lista.get(position).getTitulo()  );
        holder.lblAutor.setText(lista.get(position).getAutor());
        Boolean bt_like = lista.get(position).getMegusta();
        Glide.with(context).load(lista.get(position).getImagen()).into(holder.imgImagen);

        holder.imgMegusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenemos el estado actual de "me gusta" desde el modelo Noticia
                boolean isLiked = lista.get(position).getMegusta();

                // Invertimos el estado
                isLiked = !isLiked;

                // Actualizamos el estado en el modelo Noticia
                lista.get(position).setMegusta(isLiked);

                // Actualizamos la imagen según el nuevo estado de "me gusta"
                if (isLiked) {
                    holder.imgMegusta.setImageResource(R.drawable.heart_final);
                } else {
                    holder.imgMegusta.setImageResource(R.drawable.heart_inicial);
                }
            }

        });
        // Configuramos la imagen según el estado actual de "me gusta" al cargar la vista
        if (bt_like) {
            holder.imgMegusta.setImageResource(R.drawable.heart_final);
        } else {
            holder.imgMegusta.setImageResource(R.drawable.heart_inicial);
        }
        holder.imgImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(context, InfoNoticias.class);
                intent.putExtra("id", lista.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView lblTitulo, lblAutor;
        ImageView imgImagen, imgMegusta, imgCompartir;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblTitulo = itemView.findViewById(R.id.lblTitulo);
            lblAutor = itemView.findViewById(R.id.lblAutor);

            imgImagen = itemView.findViewById(R.id.imgImagen);
            imgMegusta = itemView.findViewById(R.id.imgMegusta);
        }
    }
}
