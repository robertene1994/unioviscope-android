package com.robert.android.unioviscope.presentation.presenters.impl;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.GetHomeScreenInteractor;
import com.robert.android.unioviscope.domain.interactors.LogInInteractor;
import com.robert.android.unioviscope.domain.interactors.UserSessionInteractor;
import com.robert.android.unioviscope.domain.interactors.impl.GetHomeScreenInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.LogInInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.UserSessionInteractorImpl;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.LogInPresenter;
import com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter;

/**
 * Clase que extiende la clase AbstractPresenter e implementa la interfaz LogInPresenter. Implementa las interfaces
 * de los callbacks de los interactors LogInInteractor, UserSessionInteractor y GetHomeScreenInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter
 * @see com.robert.android.unioviscope.presentation.presenters.LogInPresenter
 * @see com.robert.android.unioviscope.domain.interactors.LogInInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.UserSessionInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.GetHomeScreenInteractor.Callback
 */
public class LogInPresenterImpl extends AbstractPresenter implements LogInPresenter, LogInInteractor.Callback,
        UserSessionInteractor.Callback, GetHomeScreenInteractor.Callback {

    private final LogInPresenter.View mView;
    private final Context mContext;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;
    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param view               el view en el que se notifica los resultados de las operaciones realizadas.
     * @param context            el contexto de la aplicación.
     * @param service            la API con la que se realizan las comunicaciones.
     * @param sessionRepository  el repositorio de la sesión del estudiante.
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public LogInPresenterImpl(Executor executor, MainThread mainThread, View view, Context context,
                              StudentService service, SessionRepository sessionRepository,
                              SettingsRepository settingsRepository) {
        super(executor, mainThread);
        mView = view;
        mContext = context;
        mService = service;
        mSessionRepository = sessionRepository;
        mSettingsRepository = settingsRepository;
    }

    @Override
    public void resume() {
        // no aplicable
    }

    @Override
    public void pause() {
        // no aplicable
    }

    @Override
    public void stop() {
        // no aplicable
    }

    @Override
    public void destroy() {
        // no aplicable
    }

    @Override
    public void checkUserSession() {
        mView.showProgress();
        new UserSessionInteractorImpl(mExecutor, mMainThread,this, mSessionRepository).execute();
    }

    @Override
    public void checkHomeScreen() {
        new GetHomeScreenInteractorImpl(mExecutor, mMainThread,this, mSettingsRepository).execute();
    }

    @Override
    public void logIn(String userName, String password) {
        mView.showProgress();
        new LogInInteractorImpl(mExecutor, mMainThread, this,
                mContext, mService, mSessionRepository, userName, password).execute();
    }

    @Override
    public void onSuccessLogIn(User user) {
        mView.hideProgress();
        mView.onSuccessLogIn(user);
    }

    @Override
    public void onFailureLogIn() {
        mView.hideProgress();
        mView.onFailureLogIn();
    }

    @Override
    public void onRoleNotAllowedLogIn() {
        mView.hideProgress();
        mView.onRoleNotAllowedLogIn();
    }

    @Override
    public void onNoInternetConnection() {
        mView.hideProgress();
        mView.noInternetConnection();
    }

    @Override
    public void onServiceNotAvailable() {
        mView.hideProgress();
        mView.serviceNotAvailable();
    }

    @Override
    public void onUserLoggedIn() {
        mView.onUserLoggedIn();
    }

    @Override
    public void onUserNotLoggedIn() {
        mView.hideProgress();
    }

    @Override
    public void onUserSessionExpired() {
        mView.hideProgress();
        mView.onUserSessionExpired();
    }

    @Override
    public void onHomeScreenRetrieved(Boolean isHomeScreen) {
        mView.onHomeScreenRetrieved(isHomeScreen);
    }
}
