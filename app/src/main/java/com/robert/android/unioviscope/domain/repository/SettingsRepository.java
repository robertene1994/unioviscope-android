package com.robert.android.unioviscope.domain.repository;

/**
 * Interfaz del repositorio para las preferencias (ajustes de la aplicación) que el estudiante puede establecer para
 * la aplicación.
 *
 * @author Robert Ene
 */
public interface SettingsRepository {

    /**
     * Método que devuelve la preferencia establecida por el estudiante sobre la utilización del reconocimiento
     * facial en la certificación de las asistencias.
     *
     * @return la preferencia del estudiante sobre la utilización del reconocimiento facial.
     */
    Boolean getFaceRecognition();

    /**
     * Método que guarda la preferencia establecida por el estudiante sobre la utilización del reconocimiento
     * facial en la certificación de las asistencias.
     *
     * @param isFaceRecognition la preferencia del estudiante sobre la utilización del reconocimiento facial.
     */
    void saveFaceRecognition(Boolean isFaceRecognition);

    /**
     * Método que devuelve la preferencia del estudiante sobre la certificación de asistencias como pantalla
     * inicial de la aplicación.
     *
     * @return la preferencia del estudiante sobre la certificación de asistencias como pantalla
     * inicial de la aplicación.
     */
    Boolean getHomeScreen();

    /**
     * Método que guarda la preferencia del estudiante sobre la certificación de asistencias como pantalla
     * inicial de la aplicación.
     *
     * @param isHomeScreen la preferencia del estudiante sobre la certificación de asistencias como pantalla
     *                     inicial de la aplicación.
     */
    void saveHomeScreen(Boolean isHomeScreen);

    /**
     * Método que devuelve la preferencia del estudiante sobre el idioma de la aplicación.
     *
     * @return la preferencia del estudiante sobre el idioma de la aplicación.
     */
    String getLanguage();

    /**
     * Método que guarda la preferencia del estudiante sobre el idioma de la aplicación.
     *
     * @param language la preferencia del estudiante sobre el idioma de la aplicación.
     */
    void saveLanguage(String language);
}
