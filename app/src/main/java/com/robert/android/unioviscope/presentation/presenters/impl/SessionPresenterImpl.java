package com.robert.android.unioviscope.presentation.presenters.impl;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.SessionInteractor;
import com.robert.android.unioviscope.domain.interactors.impl.SessionInteractorImpl;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.types.GroupType;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.SessionPresenter;
import com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter;
import com.robert.android.unioviscope.presentation.ui.adapters.SessionAdapter;

import java.util.List;

/**
 * Clase que extiende la clase AbstractPresenter e implementa la interfaz SessionPresenter. Implementa la interfaz
 * del callback del interactor SessionInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter
 * @see com.robert.android.unioviscope.presentation.presenters.SessionPresenter
 * @see com.robert.android.unioviscope.domain.interactors.SessionInteractor.Callback
 */
public class SessionPresenterImpl extends AbstractPresenter implements SessionPresenter, SessionInteractor.Callback {

    private final SessionPresenter.View mView;
    private final Context mContext;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param view              el view en el que se notifica los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public SessionPresenterImpl(Executor executor, MainThread mainThread, View view, Context context,
                                StudentService service, SessionRepository sessionRepository) {
        super(executor, mainThread);
        mView = view;
        mContext = context;
        mService = service;
        mSessionRepository = sessionRepository;
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
    public void getSessions(Subject subject, GroupType groupType) {
        mView.showProgress();
         new SessionInteractorImpl(mExecutor, mMainThread, this, mContext,
                mService, mSessionRepository, subject, groupType).execute();
    }

    @Override
    public void onSessionsRetrieved(List<Session> sessions) {
        mView.hideProgress();
        mView.showSessions(getArrayAdapter(sessions));
    }

    @Override
    public void onSessionsEmpty() {
        mView.hideProgress();
        mView.onSessionsEmpty();
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

    private ArrayAdapter<Session> getArrayAdapter(List<Session> sessions) {
        return new SessionAdapter(mContext, R.layout.attendance_item, sessions);
    }
}
