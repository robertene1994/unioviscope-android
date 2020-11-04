package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.SessionInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.Attendance;
import com.robert.android.unioviscope.domain.model.Session;
import com.robert.android.unioviscope.domain.model.Subject;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.GroupType;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.utils.ConnectivityStatus;
import com.robert.android.unioviscope.network.converter.DateDeserializer;
import com.robert.android.unioviscope.network.service.StudentService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz SessionInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.SessionInteractor
 */
public class SessionInteractorImpl extends AbstractInteractor implements SessionInteractor {

    private final SessionInteractor.Callback mCallback;
    private final Context mContext;
    private final StudentService mService;
    private final Subject mSubject;
    private final GroupType mGroupType;
    private final User mUser;
    private Gson mGson;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     * @param subject           la asignatura para la que se recuperan las sesiones.
     * @param groupType         el tipo de sesiones (tipo de grupo) que se recuperan para la asignatura establecida.
     */
    public SessionInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Context context,
                                 StudentService service, SessionRepository sessionRepository, Subject subject,
                                 GroupType groupType) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
        mService = service;
        mUser = sessionRepository.getUser();
        mSubject = subject;
        mGroupType = groupType;
        setDeserializer();
    }

    @Override
    public void run() {
        mService.getSessions(mUser.getId(), mSubject.getId(), mGroupType).enqueue(new retrofit2.Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, final Response<List<Session>> response) {
                getAttendances((response != null && response.body() != null) ? response.body() : null);
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                handleError();
            }
        });
    }

    private void getAttendances(final List<Session> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSessionsEmpty();
                }
            });
        } else {
            for (int i = 0; i < sessions.size(); i++) {
                final int finalI = i;
                mService.getAttendance(mUser.getId(), sessions.get(finalI).getId()).enqueue(new retrofit2.Callback<ResponseBody>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Attendance attendance = null;
                        try {
                            attendance = mGson.fromJson(response.body().string(), Attendance.class);
                        } catch (IOException e) {
                            handleError();
                        }
                        sessions.get(finalI).setAttendance(attendance);
                        if (finalI == sessions.size() - 1) {
                            mMainThread.post(new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onSessionsRetrieved(sessions);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        handleError();
                    }
                });
            }
        }
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


    private void setDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        mGson = gsonBuilder.create();
    }
}
