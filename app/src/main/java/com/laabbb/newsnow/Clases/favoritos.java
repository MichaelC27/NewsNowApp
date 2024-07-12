package com.laabbb.newsnow.Clases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.laabbb.newsnow.R;

import java.util.Set;

public class favoritos extends Fragment {
    private TextView lbl_favoritos;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public favoritos() {
        // Required empty public constructor
    }

    public static favoritos newInstance(String param1, String param2) {
        favoritos fragment = new favoritos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        lbl_favoritos = view.findViewById(R.id.lbl_favoritos);

        // Obtener el nombre de usuario de SessionManager
        String username = SessionManager.getInstance().getUsername();

        // Obtener los IDs de noticias favoritas desde la base de datos
        C_DB_Likes dbLikes = new C_DB_Likes(getActivity());
        Set<String> likedIds = dbLikes.getLikedNewsIds(username);

        // Convertir el set de IDs a una cadena para mostrar
        StringBuilder sb = new StringBuilder("Favoritos:\n");
        for (String id : likedIds) {
            sb.append(id).append("\n");
        }

        // Mostrar la lista de IDs en el TextView
        lbl_favoritos.setText(sb.toString());

        return view;
    }
}
