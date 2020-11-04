package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.BaseCallbackInteractor;
import com.robert.android.unioviscope.domain.interactors.base.Interactor;
import com.robert.android.unioviscope.domain.model.Subject;

import java.util.List;

/**
 * Interfaz del interactor que se encarga de recuperar las asignaturas asociadas a un determinado estudiante.
 *
 * @author Robert Ene
 */
public interface SubjectInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback extends BaseCallbackInteractor {

        /**
         * Método que notifica que las asignaturas asociadas al estudiante establecido han sido recuperadas.
         *
         * @param subjects las asignaturas asociadas al estudiante establecido.
         */
        void onSubjectsRetrieved(List<Subject> subjects);

        /**
         * Método que notifica que el estudiante establecido no tiene asignaturas asociadas.
         */
        void onSubjectsEmpty();
    }
}
