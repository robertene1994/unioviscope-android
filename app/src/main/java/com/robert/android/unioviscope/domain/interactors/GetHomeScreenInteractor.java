package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga obtener la preferencia del estudiante que indica si se utiliza o no la
 * certificación de asistencias como pantalla inicial de la aplicación.
 *
 * @author Robert Ene
 */
public interface GetHomeScreenInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param isHomeScreen la preferencia del estudiante sobre la certificación de asistencias como pantalla
         *                     inicial de la aplicación.
         */
        void onHomeScreenRetrieved(Boolean isHomeScreen);
    }
}
