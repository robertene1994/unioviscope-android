package com.robert.android.unioviscope.presentation.presenters;

import android.widget.ArrayAdapter;

import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.types.GroupType;
import com.robert.android.unioviscope.presentation.presenters.base.BasePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.BaseViewPresenter;

/**
 * Interfaz del presenter que se encarga de coordinar la vista asociada a la recuperación de las sesiones
 * asociadas a una determinada asignatura.
 *
 * @author Robert Ene
 */
public interface SessionPresenter extends BasePresenter {

    /**
     * Interfaz que es utilizada por el presenter para notificar de los resultados de las operaciones que tiene
     * asignadas.
     *
     * @author Robert Ene
     */
    interface View extends BaseViewPresenter {

        /**
         * Método que notifica que las sesiones planificadas para una asignatura se pueden mostrar.
         *
         * @param adapter el adapter que contiene la sesiones de la asignatura.
         */
        void showSessions(ArrayAdapter<Session> adapter);

        /**
         * Método que notifica que la asignatura no tiene sesiones planificadas.
         */
        void onSessionsEmpty();
    }

    /**
     * Método que recupera las sesiones planificadas para una determinada asignatura.
     *
     * @param subject   la asignatura para la que se recupera las sesiones.
     * @param groupType el tipo de sesiones (tipo de grupo).
     */
    void getSessions(Subject subject, GroupType groupType);
}
