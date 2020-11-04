package com.robert.android.unioviscope.presentation.presenters;

import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.presentation.presenters.base.BasePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.BaseViewPresenter;

/**
 * Interfaz del presenter que se encarga de coordinar la vista asociada al inicio de sesión del estudiante.
 *
 * @author Robert Ene
 */
public interface LogInPresenter extends BasePresenter {

    /**
     * Interfaz que es utilizada por el presenter para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface View extends BaseViewPresenter {

        /**
         * Método que notifica que no se ha podido iniciar sesión.
         */
        void onFailureLogIn();

        /**
         * Método que notifica que el usuario que intenta iniciar sesión no tiene el rol de estudiante.
         */
        void onRoleNotAllowedLogIn();

        /**
         * Método que notifica que se ha iniciado sesión con éxito.
         *
         * @param user los datos del estudiante que ha iniciado sesión.
         */
        void onSuccessLogIn(User user);

        /**
         * Método que notifica que el estudiante tiene una sesión iniciada en la aplicación.
         */
        void onUserLoggedIn();

        /**
         * Método que notifica que la sesión iniciada por el estudiante ha caducado.
         */
        void onUserSessionExpired();

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param isHomeScreen la preferencia del estudiante sobre la certificación de asistencias como pantalla
         *                     inicial de la aplicación.
         */
        void onHomeScreenRetrieved(Boolean isHomeScreen);
    }

    /**
     * Método que comprueba si el estudiante ya tiene una sesión iniciada en la aplicación.
     */
    void checkUserSession();

    /**
     * Método que comprueba la preferencia del estudiante sobre la certificación de asistencias como pantalla
     * inicial de la aplicación.
     */
    void checkHomeScreen();

    /**
     * Método que inicia sesión en la aplicación a partir de sus credenciales.
     *
     * @param userName el nombre de usuario del estudiante.
     * @param password la contraseña del estudiante.
     */
    void logIn(String userName, String password);
}
