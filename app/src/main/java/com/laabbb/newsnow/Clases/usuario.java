package com.laabbb.newsnow.Clases;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.laabbb.newsnow.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class usuario extends Fragment {
    private TextView lbl_User, lbl_Nombre, lbl_Apellido, lbl_Email;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public usuario() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static usuario newInstance(String param1, String param2) {
        usuario fragment = new usuario();
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
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        lbl_User = view.findViewById(R.id.lbl_user);
        lbl_Nombre = view.findViewById(R.id.lbl_nombre);
        lbl_Apellido = view.findViewById(R.id.lbl_apellido);
        lbl_Email = view.findViewById(R.id.lbl_email);

        // Obtener el nombre de usuario de la sesión actual
        String username = SessionManager.getInstance().getUsername();
        C_DB_Noticias obj = new C_DB_Noticias(this.getActivity());
        // Llamar al método para seleccionar los datos del usuario desde la base de datos
        C_DB_Noticias usuario = obj.seleccionarDatosUsuario(username);

        // Actualizar las vistas con los datos del usuario
        if (usuario != null) {
            lbl_User.setText(usuario.getUsuario());
            lbl_Nombre.setText(usuario.getNombre());
            lbl_Apellido.setText(usuario.getApellido());
            lbl_Email.setText(usuario.getEmail());
        }
        return view;
    }
}
