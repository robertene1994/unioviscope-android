package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.LogOutInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.repository.SessionRepository;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz LogOutInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.LogOutInteractor
 */
public class LogOutInteractorImpl extends AbstractInteractor implements LogOutInteractor {

    private final LogOutInteractor.Callback mCallback;
    private final SessionRepository mSessionRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     */
    public LogOutInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                SessionRepository sessionRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mSessionRepository = sessionRepository;
    }

    @Override
    public void run() {
        mSessionRepository.deleteUser();
        mSessionRepository.deleteToken();

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onLogOut();
            }
        });
    }
}
