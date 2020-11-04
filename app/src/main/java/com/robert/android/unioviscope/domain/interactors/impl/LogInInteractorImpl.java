package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.LogInInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.AccountCredentials;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.model.types.Role;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.utils.ConnectivityStatus;
import com.robert.android.unioviscope.network.service.StudentService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz LogInInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.LogInInteractor
 */
public class LogInInteractorImpl extends AbstractInteractor implements LogInInteractor {

    private final LogInInteractor.Callback mCallback;
    private final Context mContext;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;
    private final AccountCredentials mAccount;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     * @param userName          el nombre de usuario del estudiante.
     * @param password          la contraseña del estudiante.
     */
    public LogInInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Context context,
                               StudentService service, SessionRepository sessionRepository, String userName,
                               String password) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
        mService = service;
        mSessionRepository = sessionRepository;
        mAccount = new AccountCredentials(userName, password);
    }

    @Override
    public void run() {
        mService.logIn(mAccount).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String token = response.headers().get("Authorization");
                if (token == null) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onFailureLogIn();
                        }
                    });
                } else {
                    mSessionRepository.saveToken(token);
                    getUserDetails(mAccount.getUserName());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                handleError();
            }
        });
    }

    private void getUserDetails(String userName) {
        mService.getUserDetails(userName).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = response.body();

                if (user != null && user.getRole() != null && !user.getRole().equals(Role.STUDENT)) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onRoleNotAllowedLogIn();
                        }
                    });
                } else {
                    mSessionRepository.saveUser(user);
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onSuccessLogIn(user);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
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
