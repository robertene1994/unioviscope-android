package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga obtener la preferencia del estudiante que indica si se utiliza o no el proceso
 * de reconocimiento facial durante la certificación de asistencias.
 *
 * @author Robert Ene
 */
public interface GetFaceRecognitionInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param isFaceRecognition la preferencia del estudiante sobre la utilización del reconocimiento facial.
         */
        void onFaceRecognitionRetrieved(Boolean isFaceRecognition);
    }
}
