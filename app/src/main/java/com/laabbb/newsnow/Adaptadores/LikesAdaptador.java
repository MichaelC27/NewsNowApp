package com.laabbb.newsnow.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laabbb.newsnow.Clases.Noticia;
import com.laabbb.newsnow.R;

import java.util.ArrayList;

public class LikesAdaptador extends RecyclerView.Adapter<LikesAdaptador.ViewHolder> {
    Context context;
    ArrayList<Noticia> lista;

    public LikesAdaptador(Context context, ArrayList<Noticia> lista) {
        this.context = context;
        this.lista = lista;
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
