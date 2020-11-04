package com.robert.android.unioviscope.domain.interactors.impl;

import com.auth0.android.jwt.JWT;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.UserSessionInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz UserSessionInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.UserSessionInteractor
 */
public class UserSessionInteractorImpl extends AbstractInteractor implements UserSessionInteractor {

    private final UserSessionInteractor.Callback mCallback;
    private final SessionRepository mSessionRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     */
    public UserSessionInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                     SessionRepository sessionRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mSessionRepository = sessionRepository;
    }

    @Override
    public void run() {
        final User user = mSessionRepository.getUser();
        final String token = mSessionRepository.getToken();

        if (user == null && token == null) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onUserNotLoggedIn();
                }
            });
        } else if (tokenIsExpired(token)) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onUserSessionExpired();
                }
            });
        } else {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onUserLoggedIn();
                }
            });
        }
    }

    private Boolean tokenIsExpired(String token) {
        if (token == null || token.isEmpty()) return true;
        token = token.substring(token.indexOf(" ") + 1, token.length() - 1);
        return new JWT(token).isExpired(0);
    }
}
