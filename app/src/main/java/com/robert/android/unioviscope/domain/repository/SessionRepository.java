package com.robert.android.unioviscope.domain.repository;

import com.robert.android.unioviscope.domain.model.User;

/**
 * Interfaz del repositorio para la sesión iniciada por los los estudiantes dentro de la aplicación.
 *
 * @author Robert Ene
 */
public interface SessionRepository {

    /**
     * Método que devuelve los datos del estudiante que tiene la sesión iniciada.
     *
     * @return los datos del estudiante.
     */
    User getUser();

    /**
     * Método que guarda los datos del estudiante que tiene la sesión iniciada.
     *
     * @param user los datos del estudiante.
     */
    void saveUser(User user);

    /**
     * Método que elimina los datos del estudiante que tiene la sesión iniciada.
     */
    void deleteUser();

    /**
     * Método que devuelve el token que valida la sesión del estudiante.
     *
     * @return el token de la sesión.
     */
    String getToken();

    /**
     * Método que guarda el token que valida la sesión del estudiante.
     *
     * @param token el token de la sesión.
     */
    void saveToken(String token);

    /**
     * Método que elimina el token que valida la sesión del estudiante.
     */
    void deleteToken();
}
