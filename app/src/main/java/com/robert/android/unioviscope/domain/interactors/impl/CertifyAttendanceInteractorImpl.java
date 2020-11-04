package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;
import android.graphics.Bitmap;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.CertifyAttendanceInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
import com.robert.android.unioviscope.domain.utils.ConnectivityStatus;
import com.robert.android.unioviscope.network.service.StudentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz CertifyAttendanceInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.CertifyAttendanceInteractor
 */
public class CertifyAttendanceInteractorImpl extends AbstractInteractor implements CertifyAttendanceInteractor {

    private final CertifyAttendanceInteractor.Callback mCallback;
    private final Context mContext;
    private final StudentService mService;
    private final AttendanceCertificate mAttendanceCertificate;
    private final File mPhotoFile;
    private final Bitmap mPhoto;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor              el executor de hilos.
     * @param mainThread            el hilo principal de la aplicación.
     * @param callback              el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context               el contexto de la aplicación.
     * @param service               la API con la que se realizan las comunicaciones.
     * @param attendanceCertificate el certificado de la asistencia.
     * @param photo                 la foto capturada por el estudiante.
     * @param photoFile             el fichero de la foto capturada por el estudiante.
     */
    public CertifyAttendanceInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                           Context context, StudentService service,
                                           AttendanceCertificate attendanceCertificate, Bitmap photo, File photoFile) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
        mService = service;
        mAttendanceCertificate = attendanceCertificate;
        mPhoto = photo;
        mPhotoFile = photoFile;
    }

    @Override

    public void run() {
        File photoFile;

        try {
            photoFile = bitmapToFile(mPhoto);
        } catch (IOException e) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onCertifyAttendanceError();
                }
            });
            return;
        }

        MultipartBody.Part imagePart = null;

        if (photoFile != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
            imagePart = MultipartBody.Part.createFormData("image", "image", imageBody);
        }

        mService.certifyAttendance(mAttendanceCertificate, imagePart).enqueue(new retrofit2.Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response != null && response.body() != null && response.body().equals(Boolean.TRUE)) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onSucessCertifyAttendance();
                        }
                    });
                } else {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onFailureCertifyAttendance();
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

    private File bitmapToFile(Bitmap photo) throws IOException {
        if (photo == null)
            return null;

        OutputStream outStream;

        int width = photo.getWidth();
        int height = photo.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = 1280;
            height = (int) (width / bitmapRatio);
        } else {
            height = 720;
            width = (int) (height * bitmapRatio);
        }

        photo = Bitmap.createScaledBitmap(photo, width, height, true);

        if (photo == null)
            throw new IOException();

        outStream = new FileOutputStream(mPhotoFile);
        photo.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        return mPhotoFile;
    }
}
