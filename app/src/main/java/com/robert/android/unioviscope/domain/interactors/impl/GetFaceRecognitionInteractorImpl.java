package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz DetectFaceInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor
 */
public class GetFaceRecognitionInteractorImpl extends AbstractInteractor implements GetFaceRecognitionInteractor {

    private final GetFaceRecognitionInteractor.Callback mCallback;
    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param callback           el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public GetFaceRecognitionInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                            SettingsRepository settingsRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mSettingsRepository = settingsRepository;
    }

    @Override
    public void run() {
        final Boolean isFaceRecognition = checkDefault(mSettingsRepository.getFaceRecognition());

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFaceRecognitionRetrieved(isFaceRecognition);
            }
        });
    }

    private Boolean checkDefault(Boolean isFaceRecognition) {
        if (isFaceRecognition == null) {
            isFaceRecognition = Boolean.TRUE;
            mSettingsRepository.saveFaceRecognition(Boolean.TRUE);
        }
        return isFaceRecognition;
    }
}
