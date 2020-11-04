package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.VerifyAttendanceCertificateInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.utils.ConnectivityStatus;
import com.robert.android.unioviscope.network.service.StudentService;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz VerifyAttendanceCertificateInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.VerifyAttendanceCertificateInteractor
 */
public class VerifyAttendanceCertificateInteractorImpl extends AbstractInteractor implements VerifyAttendanceCertificateInteractor {

    private final VerifyAttendanceCertificateInteractor.Callback mCallback;
    private final Context mContext;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;
    private AttendanceCertificate mAttendanceCertificate;
    private final String mQrCodeToken;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor          el executor de hilos.
     * @param mainThread        el hilo principal de la aplicación.
     * @param callback          el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context           el contexto de la aplicación.
     * @param service           la API con la que se realizan las comunicaciones.
     * @param sessionRepository el repositorio de la sesión del estudiante.
     * @param qrCodeToken       el token del código QR escaneado por el estudiante.
     */
    public VerifyAttendanceCertificateInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                                     Context context, StudentService service,
                                                     SessionRepository sessionRepository, String qrCodeToken) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
        mService = service;
        mSessionRepository = sessionRepository;
        mQrCodeToken = qrCodeToken;
    }

    @Override
    public void run() {
        User user = mSessionRepository.getUser();
        mAttendanceCertificate = new AttendanceCertificate(user.getUserName(), mQrCodeToken, new Date().getTime());

        mService.verifyAttendanceCertificate(mAttendanceCertificate).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response != null && response.body() != null && response.body().equals(Boolean.TRUE)) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onValidAttendanceCertificate(mAttendanceCertificate);
                        }
                    });
                } else {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onInvalidAttendanceCertificate();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
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
