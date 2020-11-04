package com.robert.android.unioviscope.domain.interactors.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.DetectFaceInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz DetectFaceInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.DetectFaceInteractor
 */
public class DetectFaceInteractorImpl extends AbstractInteractor implements DetectFaceInteractor {

    private final DetectFaceInteractor.Callback mCallback;
    private final FaceDetector mFaceDetector;
    private final Bitmap mPhoto;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor     el executor de hilos.
     * @param mainThread   el hilo principal de la aplicación.
     * @param callback     el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param faceDetector el detector facial que se utiliza para la detección de rostros en la foto del estudiante.
     * @param photo        la foto capturada por el estudiante.
     */
    public DetectFaceInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                    FaceDetector faceDetector, Bitmap photo) {
        super(executor, mainThread);
        mCallback = callback;
        mFaceDetector = faceDetector;
        mPhoto = photo;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void run() {
        SparseArray<Face> faces;

        if (!mFaceDetector.isOperational()) {
            onFaceDetectorNotAvailable();
        } else {
            Frame frame = new Frame.Builder().setBitmap(mPhoto).build();
            faces = mFaceDetector.detect(frame);
            mFaceDetector.release();

            if (faces != null) {
                if (faces.size() == 0) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onNoFaceDetected();
                        }
                    });
                } else if (faces.size() > 1) {
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onMoreThanOneFaceDetected();
                        }
                    });
                } else {
                    final Bitmap photo = extractFace(faces.valueAt(0), mPhoto);
                    mMainThread.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onFaceDetected(photo);
                        }
                    });
                }
            } else {
                onFaceDetectorNotAvailable();
            }
        }
    }

    private void onFaceDetectorNotAvailable() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFaceDetectorNotAvailable();
            }
        });
    }

    private Bitmap extractFace(Face face, Bitmap photo) {
        Bitmap tempPhoto = Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true)
                .copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(tempPhoto);
        Rect destBounds = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(photo, null, destBounds, null);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawRect((face.getPosition().x * 1), (face.getPosition().y * 1),
                ((face.getPosition().x + face.getWidth()) * 1),
                ((face.getPosition().y + face.getHeight()) * 1), paint);
        tempPhoto = Bitmap.createBitmap(tempPhoto,
                (int) (face.getPosition().x * 1), (int) (face.getPosition().y * 1),
                (int) ((face.getWidth()) * 1), (int) ((face.getHeight()) * 1));
        return tempPhoto;
    }
}
