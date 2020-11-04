package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.Interactor;

/**
 * Interfaz del interactor que se encarga de guardar las preferencias (ajustes de la aplicación) del estudiante.
 *
 * @author Robert Ene
 */
public interface SaveSettingsInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback {

        /**
         * Método que notifica que las preferencias (ajustes de la aplicación) han sido guardadas correctamente.
         */
        void onSavedSettings();
    }
}
