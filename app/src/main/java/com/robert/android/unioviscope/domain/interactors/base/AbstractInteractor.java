package com.robert.android.unioviscope.domain.interactors.base;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;

/**
 * Clase base para todos los interactors que contienen la lógica de la aplicación.
 *
 * @author Robert Ene
 */
public abstract class AbstractInteractor implements Interactor {

    private final Executor mExecutor;
    protected final MainThread mMainThread;

    /**
     * Instanciar un nuevo AbstractInteractor.
     *
     * @param executor   el executor que ejecuta los hilos.
     * @param mainThread el hilo en el que se ejecutan los interactors (background).
     */
    protected AbstractInteractor(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }

    /**
     * Método que contiene la lógica del interactor. Su propósito es para pruebas unitarias o de integración. En la
     * práctica, los interactors ejecutan su lógica mediante el método execute().
     */
    public abstract void run();

    public void execute() {
        mExecutor.execute(this);
    }
}
