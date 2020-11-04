package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de cerrar la sesión del estudiante.
 *
 * @author Robert Ene
 */
public interface LogOutInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que la sesión del estudiante se ha cerrado correctamente.
         */
        void onLogOut();
    }
}
