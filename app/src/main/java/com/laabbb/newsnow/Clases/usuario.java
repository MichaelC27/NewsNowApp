package com.laabbb.newsnow.Clases;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.laabbb.newsnow.Login;
import com.laabbb.newsnow.R;
import com.laabbb.newsnow.Clases.SessionManager;;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class usuario extends Fragment {
    private TextView lbl_User, lbl_Nombre, lbl_Apellido, lbl_Email;
    private Toolbar toolbar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public usuario() {
        // Required empty public constructor
    }

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        lbl_User = view.findViewById(R.id.lbl_user);
        lbl_Nombre = view.findViewById(R.id.lbl_nombre);
        lbl_Apellido = view.findViewById(R.id.lbl_apellido);
        lbl_Email = view.findViewById(R.id.lbl_email);
        toolbar = view.findViewById(R.id.toolbar_Info_N);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.boton_cerrar_session, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btn_salir) {
            // Llamar al método para cerrar sesión
            SessionManager.getInstance().logout();

            // Redirigir al usuario a la pantalla de inicio de sesión
            Intent intent = new Intent(getActivity(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
