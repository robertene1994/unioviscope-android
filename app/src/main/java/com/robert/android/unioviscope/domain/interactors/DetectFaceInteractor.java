package com.robert.android.unioviscope.domain.interactors;

import android.graphics.Bitmap;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de la detección de rostros en una foto capturada por el estudiante.
 *
 * @author Robert Ene
 */
public interface DetectFaceInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que se ha detectado un rostro en la foto capturada por el estudiante.
         *
         * @param photo la foto del estudiante en la que se ha detectado un rostro.
         */
        void onFaceDetected(Bitmap photo);

        /**
         * Método que notifica que el detector facial (API Vision de Google) no está disponible.
         */
        void onFaceDetectorNotAvailable();

        /**
         * Método que notifica que no se ha detectado ningún rostro en la foto capturada por el estudiante.
         */
        void onNoFaceDetected();

        /**
         * Método que notifica que se ha detectado más de un rostro en la foto capturada por el estudiante.
         */
        void onMoreThanOneFaceDetected();
    }
}
