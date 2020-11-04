package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.SaveSettingsInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz SaveSettingsInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.SaveSettingsInteractor
 */
public class SaveFaceRecognitionInteractorImpl extends AbstractInteractor implements SaveSettingsInteractor {

    private final SaveSettingsInteractor.Callback mCallback;
    private final SettingsRepository mSettingsRepository;
    private final Boolean mIsFaceRecognition;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param callback           el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     * @param isFaceRecognition  la preferencia del estudiante sobre la utilización del reconocimiento facial.
     */
    public SaveFaceRecognitionInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                             SettingsRepository settingsRepository, Boolean isFaceRecognition) {
        super(executor, mainThread);
        mCallback = callback;
        mSettingsRepository = settingsRepository;
        mIsFaceRecognition = isFaceRecognition;
    }

    @Override
    public void run() {
        mSettingsRepository.saveFaceRecognition(mIsFaceRecognition);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSavedSettings();
            }
        });
    }
}
