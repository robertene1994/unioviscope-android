package com.robert.android.unioviscope.domain.interactors.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.TakePhotoInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;

import java.io.File;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz TakePhotoInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.TakePhotoInteractor
 */
public class TakePhotoInteractorImpl extends AbstractInteractor implements TakePhotoInteractor {

    private static final String FILE_PROVIDER = "com.robert.android.unioviscope.fileprovider";
    private static final String PHOTO_NAME = "temp";
    private static final String PHOTO_FORMAT = ".jpg";

    private final TakePhotoInteractor.Callback mCallback;
    private final Context mContext;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor   el executor de hilos.
     * @param mainThread el hilo principal de la aplicación.
     * @param callback   el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param context    el contexto de la aplicación.
     */
    public TakePhotoInteractorImpl(Executor executor, MainThread mainThread, Callback callback, Context context) {
        super(executor, mainThread);
        mCallback = callback;
        mContext = context;
    }

    @Override
    public void run() {
        File photoFile;
        Uri photoUri;

        try {
            File storageDirectory = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            onStorageDirectoryRetrieved(storageDirectory);
            photoFile = File.createTempFile(PHOTO_NAME, PHOTO_FORMAT, storageDirectory);
            onPhotoFileRetrieved(photoFile);
            onPhotoPathRetrieved(photoFile.getAbsolutePath());
            photoUri = FileProvider.getUriForFile(mContext, FILE_PROVIDER, photoFile);
        } catch (Exception ex) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onTakePhotoError();
                }
            });
            return;
        }

        final Uri finalPhotoUri = photoUri;
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onTakePhotoPrepared(finalPhotoUri);
            }
        });
    }

    private void onStorageDirectoryRetrieved(final File storageDirectory) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onStorageDirectoryRetrieved(storageDirectory);
            }
        });
    }

    private void onPhotoFileRetrieved(final File photoFile) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onPhotoFileRetrieved(photoFile);
            }
        });
    }

    private void onPhotoPathRetrieved(final String photoPath) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onPhotoPathRetrieved(photoPath);
            }
        });
    }
}
