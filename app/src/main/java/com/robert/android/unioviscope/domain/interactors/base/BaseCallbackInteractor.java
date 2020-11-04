package com.robert.android.unioviscope.domain.interactors.base;

/**
 * Interfaz base para las interfaces Callback de los interactors. Contiene métodos compartidos por las interfaces
 * Callback de los interactors que se comunican con una API.
 *
 * @author Robert Ene
 */
public interface BaseCallbackInteractor {

    /**
     * Método que notifica que el dispositivo móvil no está conectado a Internet.
     */
    void onNoInternetConnection();

    /**
     * Método que notifica que el servicio (API) con el que se comunica no está disponible por cualquier razón.
     */
    void onServiceNotAvailable();
}
