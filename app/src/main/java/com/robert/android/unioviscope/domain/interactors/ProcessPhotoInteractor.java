package com.robert.android.unioviscope.domain.interactors;

import android.graphics.Bitmap;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de procesar la foto capturada por el estudiante para la certificación de la
 * asistencia.
 *
 * @author Robert Ene
 */
public interface ProcessPhotoInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que la foto capturada por el estudiante ha sido procesada correctamente.
         *
         * @param photo la foto del estudiante que ha sido procesada.
         */
        void onProcessedPhoto(Bitmap photo);

        /**
         * Método que notifica que ha ocurrido un error durante el procesamiento de la foto del estudiante.
         */
        void onProcessPhotoError();
    }
}
