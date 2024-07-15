package com.laabbb.newsnow.Clases;

/**
 * Clase singleton para manejar la sesión del usuario.
 */
public class SessionManager {
    private static SessionManager instance; // Instancia única de SessionManager
    private String username; // Nombre de usuario actualmente en sesión

    // Constructor privado para evitar instanciación directa
    private SessionManager() {}

    /**
     * Método estático para obtener la instancia única de SessionManager.
     * Utiliza sincronización para asegurar la creación de una única instancia.
     * @return Instancia única de SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Método para obtener el nombre de usuario actual en sesión.
     * @return Nombre de usuario actual
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método para establecer el nombre de usuario en sesión.
     * @param username Nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Método para cerrar la sesión del usuario.
     * Limpia el nombre de usuario de la sesión.
     */
    public void logout() {
        this.username = null;
    }
}
