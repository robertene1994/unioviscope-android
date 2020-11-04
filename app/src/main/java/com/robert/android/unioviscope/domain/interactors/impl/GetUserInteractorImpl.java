package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.GetUserInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz GetUserInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.GetUserInteractor
 */
public class GetUserInteractorImpl extends AbstractInteractor implements GetUserInteractor {

    private final GetUserInteractor.Callback mCallback;
    private final SessionRepository mSessionRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     */
    public GetUserInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                 SessionRepository sessionRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mSessionRepository = sessionRepository;
    }

    @Override
    public void run() {
        final User user = mSessionRepository.getUser();

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onUserRetrieved(user);
            }
        });
    }
}
