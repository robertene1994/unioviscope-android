package com.robert.android.unioviscope.presentation.presenters.base;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;

/**
 * Clase base abstracta para todos los presenters que se comunican con interactors.
 *
 * @author Robert Ene
 */
public abstract class AbstractPresenter {

    protected final Executor mExecutor;
    protected final MainThread mMainThread;

    /**
     * Instanciar un nuevo AbstractPresenter.
     *
     * @param executor   el executor encargado de ejecutar los hilos.
     * @param mainThread el hilo en el que se ejecutan los interactors en background.
     */
    protected AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
