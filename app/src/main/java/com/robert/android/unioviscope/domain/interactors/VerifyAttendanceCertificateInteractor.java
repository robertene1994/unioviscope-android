package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.BaseCallbackInteractor;
import com.robert.android.unioviscope.domain.interactors.base.Interactor;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;

/**
 * Interfaz del interactor que se encarga de comprobar la validez del certificado de la asistencia del estudiante.
 *
 * @author Robert Ene
 */
public interface VerifyAttendanceCertificateInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback extends BaseCallbackInteractor {

        /**
         * Método que notifica que el certificado de la asistencia es válido.
         *
         * @param attendanceCertificate el certificado de la asistencia.
         */
        void onValidAttendanceCertificate(AttendanceCertificate attendanceCertificate);

        /**
         * Método que notifica que el certificado de la asistencia es inválido.
         */
        void onInvalidAttendanceCertificate();
    }
}
