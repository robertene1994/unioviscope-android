package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.BaseCallbackInteractor;
import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de certificar la asistencia de un estudiante.
 *
 * @author Robert Ene
 */
public interface CertifyAttendanceInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback extends BaseCallbackInteractor {

        /**
         * Método que notifica que la asistencia del estudiante ha sido certificada con éxito.
         */
        void onSucessCertifyAttendance();

        /**
         * Método que notifica que la asistencia del estudiante no se ha certificado con éxito.
         */
        void onFailureCertifyAttendance();

        /**
         * Método que notifica que ha ocurrido un error durante la certificación de la asistencia del estudiante.
         */
        void onCertifyAttendanceError();
    }
}
