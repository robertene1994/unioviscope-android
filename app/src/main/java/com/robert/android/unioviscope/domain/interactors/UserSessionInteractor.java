package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de comprobar el estado de la sesión del estudiante.
 *
 * @author Robert Ene
 */
public interface UserSessionInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notifica los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que el estudiante tiene una sesión iniciada en la aplicación.
         */
        void onUserLoggedIn();

        /**
         * Método que notifica que no hay ningún estudiante con una sesión iniciada en la aplicación.
         */
        void onUserNotLoggedIn();

        /**
         * Método que notifica que la sesión iniciada por el estudiante ha caducado.
         */
        void onUserSessionExpired();
    }
}
