package com.robert.android.unioviscope.presentation.presenters;

import android.widget.ArrayAdapter;

import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.presentation.presenters.base.BasePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.BaseViewPresenter;

/**
 * Interfaz del presenter que se encarga de coordinar la vista asociada a la recuperación de las asignaturas
 * asociadas al estudiante.
 *
 * @author Robert Ene
 */
public interface SubjectPresenter extends BasePresenter {

    /**
     * Interfaz que es utilizada por el presenter para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface View extends BaseViewPresenter {

        /**
         * Método que notifica que las asignaturas del estudiante se pueden mostrar.
         *
         * @param adapter el adapter que contiene las asignaturas del estudiante.
         */
        void showSubjects(ArrayAdapter<String> adapter);

        /**
         * Método que notifica que el estudiante no tiene asignaturas asociadas.
         */
        void onSubjectsEmpty();
    }

    /**
     * Método que recupera las asignaturas asociadas a un estudiante.
     */
    void getSubjects();

    /**
     * Método que devuelve una determinada asignatura de una lista en función de su posición.
     *
     * @param index el índice de la asignatura dentro de la lista.
     * @return la asignatura en función del índice.
     */
    Subject getSubjectByIndex(int index);
}
