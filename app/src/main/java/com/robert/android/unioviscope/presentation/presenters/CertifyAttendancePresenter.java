package com.robert.android.unioviscope.presentation.presenters;

import android.net.Uri;

import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.presentation.presenters.base.BasePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.BaseViewPresenter;

/**
 * Interfaz del presenter que se encarga de coordinar la vista asociada a la certificación de asistencias.
 *
 * @author Robert Ene
 */
public interface CertifyAttendancePresenter extends BasePresenter {

    /**
     * Interfaz que es utilizada por el presenter para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface View extends BaseViewPresenter {
        /**
         * Método que notifica que los datos del usuario con la sesión iniciada han sido recuperados.
         *
         * @param user los datos del usuario con la sesión iniciada.
         */
        void onUserRetrieved(User user);

        /**
         * Método que notifica que ha ocurrido un error durante la preparación de la captura de la foto del estudiante.
         */
        void onTakePhotoError();

        /**
         * Método que notifica que se ha llevado a cabo la preparación de la captura de la foto del estudiante.
         *
         * @param photoUri la URI en la que el dispositivo móvil debe almacenar la foto capturada por el estudiante.
         */
        void onTakePhotoPrepared(Uri photoUri);

        /**
         * Método que notifica que se deben comprobar permisos en el dispositivo móvil antes de realizar ninguna
         * operación.
         */
        void checkCameraPermissions();

        /**
         * Método que notifica que el certificado de la asistencia es inválido.
         */
        void onInvalidAttendanceCertificate();

        /**
         * Método que notifica que ha ocurrido un error durante el procesamiento de la foto del estudiante.
         */
        void onProcessPhotoError();

        /**
         * Método que notifica que el detector facial (API Vision de Google) no está disponible.
         */
        void onFaceDetectorNotAvailable();

        /**
         * Método que notifica que se ha detectado más de un rostro en la foto capturada por el estudiante.
         */
        void onMoreThanOneFaceDetected();

        /**
         * Método que notifica que no se ha detectado ningún rostro en la foto capturada por el estudiante.
         */
        void onNoFaceDetected();

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

        /**
         * Método que notifica que la sesión del estudiante se ha cerrado correctamente.
         */
        void onLogOut();
    }

    /**
     * Método que recupera los datos de la sesión del usuario.
     */
    void getUser();

    /**
     * Método que inicia el proceso de validación del código QR escaneado por el estudiante.
     *
     * @param qrCodeToken el token del código QR escaneado por el estudiante.
     */
    void processQrCode(String qrCodeToken);

    /**
     * método que inicia el proceso de captura de la foto del estudiante.
     */
    void takePhoto();

    /**
     * Método que inicia el proceso de la foto caputrada por el estudiante.
     */
    void processPhoto();

    /**
     * Método que inicia el proceso para cerrar la sesión del estudiante.
     */
    void logOut();
}
