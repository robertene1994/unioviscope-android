package com.robert.android.unioviscope.domain.interactors;

/**
 * Interfaz del interactor síncrono que se encarga obtener la preferencia del estudiante que indica el idioma de la
 * aplicación. Se utiliza únicamente para establecer el idioma de la aplicación (no es posible utilizar métodos
 * asíncronos durante la creación de las activities de la aplicación).
 *
 * @author Robert Ene
 */
public interface GetLanguageSynchronousInteractor {

    /**
     * Método que recupera el idioma establecido por el estudiante como preferencia.
     *
     * @return el idioma de la aplicación establecido como preferencia por el estudiante.
     */
    String getLanguage();
}
