package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.BaseCallbackInteractor;
import com.robert.android.unioviscope.domain.interactors.base.Interactor;
import com.robert.android.unioviscope.domain.model.Session;

import java.util.List;

/**
 * Interfaz del interactor que se encarga de recuperar las sesiones planificadas para una determinada asignatura.
 *
 * @author Robert Ene
 */
public interface SessionInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback extends BaseCallbackInteractor {

        /**
         * Método que notifica que las sesiones planificadas para la asignatura establecida han sido recuperados.
         *
         * @param sessions las sesiones planificadas para la asignatura establecida.
         */
        void onSessionsRetrieved(List<Session> sessions);

        /**
         * Método que notifica que la asignatura establecida no tiene sesiones planificadas.
         */
        void onSessionsEmpty();
    }
}
