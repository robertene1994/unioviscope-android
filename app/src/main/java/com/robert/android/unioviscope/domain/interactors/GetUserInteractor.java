package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;
import com.robert.android.unioviscope.domain.model.User;

/**
 * Interfaz del interactor que se encarga de obtener los datos del usuario que ha iniciado sesión en la aplicación.
 *
 * @author Robert Ene
 */
public interface GetUserInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que se ha recuperado los datos de la sesión del estudiante.
         *
         * @param user los datos del estudiante que tiene la sesión iniciada.
         */
        void onUserRetrieved(User user);
    }
}
