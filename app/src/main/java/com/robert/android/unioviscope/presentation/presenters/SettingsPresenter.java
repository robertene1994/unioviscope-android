package com.robert.android.unioviscope.presentation.presenters;

import android.widget.SpinnerAdapter;

import com.robert.android.unioviscope.presentation.presenters.base.BasePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.BaseViewPresenter;

/**
 * Interfaz del presenter que se encarga de coordinar la vista asociada a la las preferencias (ajustes de la
 * aplicación) del estudiante para la aplicación.
 *
 * @author Robert Ene
 */
public interface SettingsPresenter extends BasePresenter {

    /**
     * Interfaz que es utilizada por el presenter para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface View extends BaseViewPresenter {

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param isFaceRecognition la preferencia del estudiante sobre la utilización del reconocimiento facial.
         */
        void onFaceRecognitionRetrieved(Boolean isFaceRecognition);

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param isHomeScreen la preferencia del estudiante sobre la certificación de asistencias como pantalla
         *                     inicial de la aplicación.
         */
        void onHomeScreenRetrieved(Boolean isHomeScreen);

        /**
         * Método que notifica que se ha recuperado la preferencia del estudiante.
         *
         * @param language la preferencia del estudiante sobre el idioma de la aplicación.
         */
        void onLanguageRetrieved(String language);
    }

    /**
     * Método que devuelve el adapter para los idiomas disponibles en la aplicación en función del idioma establecido.
     *
     * @param locale el idioma establecido por el estudiante.
     * @return el adapter de los idiomas disponibles.
     */
    SpinnerAdapter getLanguagesAdapter(String locale);

    /**
     * Método que devuelve el índice dentro de una lista de un determinado idioma.
     *
     * @param locale el idioma establecido.
     * @return el índice del idioma establecido.
     */
    Integer getIndexByLocale(String locale);

    /**
     * Método que recupera la preferencia del estudiante que indica si se utiliza o no el proceso de reconocimiento
     * facial durante la certificación de asistencias.
     */
    void getFaceRecognition();

    /**
     * Método que guarda la preferencia del estudiante que indica si se utiliza o no el proceso de reconocimiento
     * facial durante la certificación de asistencias.
     *
     * @param isFaceRecognition la preferencia del estudiante sobre la utilización del reconocimiento facial.
     */
    void saveFaceRecognition(Boolean isFaceRecognition);

    /**
     * Método que recupera la preferencia del estudiante que indica si se utiliza o no la certificación de
     * asistencias como pantalla inicial de la aplicación.
     */
    void getHomeScreen();

    /**
     * Método que guarda la preferencia del estudiante que indica si se utiliza o no la certificación de
     * asistencias como pantalla inicial de la aplicación.
     *
     * @param isHomeScreen la preferencia del estudiante sobre la certificación de asistencias como pantalla
     *                     inicial de la aplicación.
     */
    void saveHomeScreen(Boolean isHomeScreen);

    /**
     * Método que recupera la preferencia del estudiante que indica el idioma de la aplicación.
     */
    void getLanguage();

    /**
     * Método que guarda la preferencia del estudiante que indica el idioma de la aplicación y comprueba el índice de
     * la misma dentro de una lista (para decidir si se actualiza el idioma de la aplicación o no).
     *
     * @param language la preferencia del estudiante sobre el idioma de la aplicación.
     * @param index    el índice del idioma dentro de la lista
     * @return true si la aplicación debe actualizar el idioma, false de lo contrario.
     */
    Boolean saveLanguageByIndex(String language, Integer index);
}
