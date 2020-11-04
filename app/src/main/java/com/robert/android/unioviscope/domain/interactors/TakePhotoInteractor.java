package com.robert.android.unioviscope.domain.interactors;

import android.net.Uri;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

import java.io.File;

/**
 * Interfaz del interactor que se encarga de preparar la captura de la foto del estudiante para la certificación de
 * la asistencia.
 *
 * @author Robert Ene
 */
public interface TakePhotoInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que se ha llevado a cabo la preparación de la captura de la foto del estudiante.
         *
         * @param photoUri la URI en la que el dispositivo móvil debe almacenar la foto capturada por el estudiante.
         */
        void onTakePhotoPrepared(Uri photoUri);

        /**
         * Método que notifica que el directorio de la foto que el estudiante captura ha sido recuperado.
         *
         * @param storageDirectory el directorio de la foto que el estudiante captura.
         */
        void onStorageDirectoryRetrieved(File storageDirectory);

        /**
         * Método que notifica que el fichero de la foto que el estudiante captura ha sido recuperado.
         *
         * @param photoFile el fichero de la foto que el estudiante captura.
         */
        void onPhotoFileRetrieved(File photoFile);

        /**
         * Método que notifica que la ruta de la foto que el estudiante captura ha sido recuperada.
         *
         * @param photoPath la ruta de la foto que el estudiante captura.
         */
        void onPhotoPathRetrieved(String photoPath);

        /**
         * Método que notifica que ha ocurrido un error durante la preparación de la captura de la foto del estudiante.
         */
        void onTakePhotoError();
    }
}
