package com.robert.android.unioviscope.presentation.presenters.base;

/**
 * Interfaz base para las interfaces View de los presenters. Contiene métodos compartidos por las interfaces
 * View de los presenters..
 *
 * @author Robert Ene
 */
public interface BaseViewPresenter {

    /**
     * Método que muestra progreso durante el tiempo en el que la aplicación está realizando operaciones en background.
     */
    void showProgress();

    /**
     * Método que oculta el progreso cuando la aplicación finaliza las operaciones en background.
     */
    void hideProgress();


    /**
     * Método que notifica que el dispositivo móvil no tiene acceso a Internet.
     */
    void noInternetConnection();

    /**
     * Método que notifica que el servico (API) no está disponible por cualquier razón.
     */
    void serviceNotAvailable();
}
