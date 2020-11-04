package com.robert.android.unioviscope.presentation.presenters.impl;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.SubjectInteractor;
import com.robert.android.unioviscope.domain.interactors.impl.SubjectInteractorImpl;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.SubjectPresenter;
import com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter;

import java.util.List;

/**
 * Clase que extiende la clase AbstractPresenter e implementa la interfaz SubjectPresenter. Implementa la interfaz
 * del callback del interactor SubjectInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter
 * @see com.robert.android.unioviscope.presentation.presenters.SubjectPresenter
 * @see com.robert.android.unioviscope.domain.interactors.SubjectInteractor.Callback
 */
public class SubjectPresenterImpl extends AbstractPresenter implements SubjectPresenter, SubjectInteractor.Callback {

    private final SubjectPresenter.View mView;
    private final Context mContext;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;
    private List<Subject> mSubjects;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param view              el view en el que se notifica los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     */
    public SubjectPresenterImpl(Executor executor, MainThread mainThread, View view, Context context,
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
    public void getSubjects() {
        mView.showProgress();
        new SubjectInteractorImpl(mExecutor, mMainThread,
                this, mContext, mService, mSessionRepository).execute();
    }

    @Override
    public Subject getSubjectByIndex(int index) {
        return mSubjects.get(index);
    }

    @Override
    public void onSubjectsRetrieved(List<Subject> subjects) {
        mSubjects = subjects;
        ArrayAdapter<String> adapter = getArrayAdapter();
        mView.showSubjects(adapter);
        mView.hideProgress();
    }

    @Override
    public void onSubjectsEmpty() {
        mView.hideProgress();
        mView.onSubjectsEmpty();
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

    private ArrayAdapter<String> getArrayAdapter() {
        String[] listItems = new String[mSubjects.size()];
        for (int i = 0; i < mSubjects.size(); i++)
            listItems[i] = mSubjects.get(i).getDenomination();
        return new ArrayAdapter<>(mContext, R.layout.subject_item, listItems);
    }
}
