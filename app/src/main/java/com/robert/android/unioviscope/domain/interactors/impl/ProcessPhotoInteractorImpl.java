package com.robert.android.unioviscope.domain.interactors.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;

import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.ProcessPhotoInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;

import java.io.IOException;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz ProcessPhotoInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.ProcessPhotoInteractor
 */
public class ProcessPhotoInteractorImpl extends AbstractInteractor implements ProcessPhotoInteractor {

    private static final String TAG_180_DEGREES = "3";
    private static final Integer ROTATE_180_DEGREES = 180;
    private static final String TAG_90_DEGREES = "6";
    private static final Integer ROTATE_90_DEGREES = 90;
    private static final String TAG_270_DEGREES = "8";
    private static final Integer ROTATE_270_DEGREES = 270;

    private final ProcessPhotoInteractor.Callback mCallback;
    private final String mPhotoPath;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor   el executor de hilos.
     * @param mainThread el hilo principal de la aplicación.
     * @param callback   el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param photoPath  la ruta de la foto capturada por el estudiante.
     */
    public ProcessPhotoInteractorImpl(Executor executor, MainThread mainThread, Callback callback, String photoPath) {
        super(executor, mainThread);
        mCallback = callback;
        mPhotoPath = photoPath;
    }

    @Override
    public void run() {
        Bitmap photo = BitmapFactory.decodeFile(mPhotoPath);
        try {
            photo = correctRotation(mPhotoPath, photo);
        } catch (IOException e) {
            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onProcessPhotoError();
                }
            });
            return;
        }

        final Bitmap finalPhoto = photo;
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onProcessedPhoto(finalPhoto);
            }
        });
    }

    private Bitmap correctRotation(String photoPath, Bitmap photo) throws IOException {
        ExifInterface exif = new ExifInterface(photoPath);
        if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase(TAG_180_DEGREES))
            photo = TransformationUtils.rotateImage(photo, ROTATE_180_DEGREES);
        else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase(TAG_90_DEGREES))
            photo = TransformationUtils.rotateImage(photo, ROTATE_90_DEGREES);
        else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase(TAG_270_DEGREES))
            photo = TransformationUtils.rotateImage(photo, ROTATE_270_DEGREES);
        return photo;
    }
}
