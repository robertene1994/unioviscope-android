package com.robert.android.unioviscope.domain.interactors;

import com.robert.android.unioviscope.domain.interactors.base.BaseCallbackInteractor;
import com.robert.android.unioviscope.domain.interactors.base.Interactor;
import com.robert.android.unioviscope.domain.model.User;

/**
 * Interfaz del interactor que se encarga de iniciar la sesión de un usuario a partir de sus credenciales.
 *
 * @author Robert Ene
 */
public interface LogInInteractor extends Interactor {

    /**
     * Interfaz que es utilizada por el interactor para notificar los resultados de las operaciones que tiene asignadas.
     *
     * @author Robert Ene
     */
    interface Callback extends BaseCallbackInteractor {

        /**
         * Método que notifica que se ha iniciado sesión con éxito utilizando las credenciales del estudiante.
         *
         * @param user los datos del estudiante que ha iniciado sesión.
         */
        void onSuccessLogIn(User user);

        /**
         * Método que notifica que no se ha podido iniciar sesión utilizando las credenciales del estudiante dado a
         * que son inválidos.
         */
        void onFailureLogIn();

        /**
         * Método que notifica que el usuario que intenta iniciar sesión no tiene el rol de estudiante (puede ser el
         * administrador o el docente).
         */
        void onRoleNotAllowedLogIn();
    }
}
