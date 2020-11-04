package com.robert.android.unioviscope.domain.executor;

import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;

/**
 * Interfaz Executor responsable de la ejecución de los interactors en los hilos backgorund.
 *
 * @author Robert Ene
 */
public interface Executor {

    /**
     * Método que ejecuta la lógica del interactor.
     *
     * @param interactor el interactor que se ejecuta.
     */
    void execute(final AbstractInteractor interactor);
}
