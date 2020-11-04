package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.SubjectInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.utils.ConnectivityStatus;
import com.robert.android.unioviscope.network.service.StudentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz SubjectInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.SubjectInteractor
 */
public class SubjectInteractorImpl extends AbstractInteractor implements SubjectInteractor {

    private final SubjectInteractor.Callback mCallback;
    private final Context mContext;
    private final StudentService mService;
    private final User mUser;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     */
    public SubjectInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Context context,
                                 StudentService service, SessionRepository sessionRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
        mService = service;
        mUser = sessionRepository.getUser();
    }

    @Override
    public void run() {
        mService.getSubjects(mUser.getId()).enqueue(new retrofit2.Callback<List<Subject>>() {
            @Override
            public void onResponse(Call<List<Subject>> call, final Response<List<Subject>> response) {
                final List<Subject> subjects = (response != null && response.body() != null) ? response.body() : null;
                if (subjects == null || subjects.isEmpty()) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onSubjectsEmpty();
                        }
                    });
                } else {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onSubjectsRetrieved(subjects);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Subject>> call, Throwable t) {
                handleError();
            }
        });
    }

    private void handleError() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                if (!ConnectivityStatus.isInternetConnection(mContext)) {
                    mCallback.onNoInternetConnection();
                } else {
                    mCallback.onServiceNotAvailable();
                }
            }
        });
    }
}
