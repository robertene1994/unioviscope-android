package com.robert.android.unioviscope.presentation.presenters.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.vision.face.FaceDetector;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.CertifyAttendanceInteractor;
import com.robert.android.unioviscope.domain.interactors.DetectFaceInteractor;
import com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor;
import com.robert.android.unioviscope.domain.interactors.GetUserInteractor;
import com.robert.android.unioviscope.domain.interactors.LogOutInteractor;
import com.robert.android.unioviscope.domain.interactors.ProcessPhotoInteractor;
import com.robert.android.unioviscope.domain.interactors.TakePhotoInteractor;
import com.robert.android.unioviscope.domain.interactors.VerifyAttendanceCertificateInteractor;
import com.robert.android.unioviscope.domain.interactors.impl.CertifyAttendanceInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.DetectFaceInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.GetFaceRecognitionInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.GetUserInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.LogOutInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.ProcessPhotoInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.TakePhotoInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.VerifyAttendanceCertificateInteractorImpl;
import com.robert.android.unioviscope.domain.model.AttendanceCertificate;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.network.service.StudentService;
import com.robert.android.unioviscope.presentation.presenters.CertifyAttendancePresenter;
import com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter;

import java.io.File;

/**
 * Clase que extiende la clase AbstractPresenter e implementa la interfaz CertifyAttendancePresenter. Implementa las
 * interfaces de los callbacks de los interactors LogInInteractor, UserSessionInteractor y GetHomeScreenInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter
 * @see com.robert.android.unioviscope.presentation.presenters.LogInPresenter
 * @see com.robert.android.unioviscope.domain.interactors.GetUserInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.TakePhotoInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.VerifyAttendanceCertificateInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.ProcessPhotoInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.DetectFaceInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.CertifyAttendanceInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.LogOutInteractor.Callback
 */
public class CertifyAttendancePresenterImpl extends AbstractPresenter implements CertifyAttendancePresenter,
        GetUserInteractor.Callback, TakePhotoInteractor.Callback, VerifyAttendanceCertificateInteractor.Callback,
        GetFaceRecognitionInteractor.Callback, ProcessPhotoInteractor.Callback, DetectFaceInteractor.Callback,
        CertifyAttendanceInteractor.Callback, LogOutInteractor.Callback {

    private final CertifyAttendancePresenter.View mView;
    private final Context mContext;
    private final FaceDetector mFaceDetector;
    private final StudentService mService;
    private final SessionRepository mSessionRepository;
    private final SettingsRepository mSettingsRepository;
    private AttendanceCertificate mAttendanceCertificate;
    private File mStorageDirectory;
    private File mPhotoFile;
    private String mPhotoPath;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param view               el view en el que se notifica los resultados de las operaciones realizadas.
     * @param context            el contexto de la aplicación.
     * @param faceDetector       el detector facial que se utiliza para la detección de rostros en la foto del
     *                           estudiante.
     * @param service            la API con la que se realizan las comunicaciones.
     * @param sessionRepository  el repositorio de la sesión del estudiante.
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public CertifyAttendancePresenterImpl(Executor executor, MainThread mainThread, View view, Context context,
                                          FaceDetector faceDetector, StudentService service,
                                          SessionRepository sessionRepository, SettingsRepository settingsRepository) {
        super(executor, mainThread);
        mView = view;
        mContext = context;
        mFaceDetector = faceDetector;
        mService = service;
        mSessionRepository = sessionRepository;
        mSettingsRepository = settingsRepository;
    }

    @Override
    public void resume() {
        // not aplicable
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
        clearPhotoDirectory(mStorageDirectory);
    }

    @Override
    public void onUserRetrieved(User user) {
        mView.hideProgress();
        mView.onUserRetrieved(user);
    }

    @Override
    public void processQrCode(String qrCodeToken) {
        mView.showProgress();
        new VerifyAttendanceCertificateInteractorImpl(mExecutor, mMainThread,
                this, mContext, mService, mSessionRepository, qrCodeToken).execute();
    }

    @Override
    public void processPhoto() {
        mView.showProgress();
        new ProcessPhotoInteractorImpl(mExecutor, mMainThread, this, mPhotoPath).execute();
    }

    @Override
    public void logOut() {
        mView.showProgress();
        new LogOutInteractorImpl(mExecutor, mMainThread, this, mSessionRepository).execute();
    }

    @Override
    public void getUser() {
        mView.showProgress();
        new GetUserInteractorImpl(mExecutor, mMainThread, this, mSessionRepository).execute();
    }

    @Override
    public void takePhoto() {
        mView.showProgress();
        new TakePhotoInteractorImpl(mExecutor, mMainThread, this, mContext).execute();
    }

    @Override
    public void onTakePhotoPrepared(Uri photoUri) {
        mView.hideProgress();
        mView.onTakePhotoPrepared(photoUri);
    }

    @Override
    public void onTakePhotoError() {
        mView.hideProgress();
        mView.onTakePhotoError();
    }

    @Override
    public void onStorageDirectoryRetrieved(File storageDirectory) {
        mStorageDirectory = storageDirectory;
    }

    @Override
    public void onPhotoPathRetrieved(String photoPath) {
        mPhotoPath = photoPath;
    }

    @Override
    public void onPhotoFileRetrieved(File photoFile) {
        mPhotoFile = photoFile;
    }

    @Override
    public void onValidAttendanceCertificate(AttendanceCertificate attendanceCertificate) {
        mView.showProgress();
        mAttendanceCertificate = attendanceCertificate;
        new GetFaceRecognitionInteractorImpl(mExecutor, mMainThread, this, mSettingsRepository).execute();
    }

    @Override
    public void onFaceRecognitionRetrieved(Boolean isFaceRecognition) {
        mView.hideProgress();

        if (isFaceRecognition) {
            mView.checkCameraPermissions();
        } else {
            mView.showProgress();
            new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, this, mContext,
                    mService, mAttendanceCertificate, null, mPhotoFile).execute();
        }
    }

    @Override
    public void onInvalidAttendanceCertificate() {
        mView.hideProgress();
        mView.onInvalidAttendanceCertificate();
    }

    @Override
    public void onProcessedPhoto(Bitmap photo) {
        mView.showProgress();
        new DetectFaceInteractorImpl(mExecutor, mMainThread, this, mFaceDetector, photo).execute();
    }

    @Override
    public void onProcessPhotoError() {
        mView.hideProgress();
        mView.onProcessPhotoError();
    }

    @Override
    public void onFaceDetected(Bitmap photo) {
        mView.showProgress();
        new CertifyAttendanceInteractorImpl(mExecutor, mMainThread, this,
                mContext, mService, mAttendanceCertificate, photo, mPhotoFile).execute();
    }

    @Override
    public void onFaceDetectorNotAvailable() {
        mView.hideProgress();
        mView.onFaceDetectorNotAvailable();
    }

    @Override
    public void onNoFaceDetected() {
        mView.hideProgress();
        mView.onNoFaceDetected();
    }

    @Override
    public void onMoreThanOneFaceDetected() {
        mView.hideProgress();
        mView.onMoreThanOneFaceDetected();
    }

    @Override
    public void onSucessCertifyAttendance() {
        mView.hideProgress();
        mView.onSucessCertifyAttendance();
    }

    @Override
    public void onFailureCertifyAttendance() {
        mView.hideProgress();
        mView.onFailureCertifyAttendance();
    }

    @Override
    public void onCertifyAttendanceError() {
        mView.hideProgress();
        mView.onCertifyAttendanceError();
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
    public void onLogOut() {
        mView.hideProgress();
        mView.onLogOut();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void clearPhotoDirectory(File fileOrDirectory) {
        if (fileOrDirectory != null) {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    clearPhotoDirectory(child);
            fileOrDirectory.delete();
        }
    }
}
