package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.GetLanguageInteractor;
import com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;

import java.util.Locale;

/**
 * Clase que extiende la clase AbstractInteractor e implementa la interfaz GetLanguageInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.base.AbstractInteractor
 * @see com.robert.android.unioviscope.domain.interactors.GetLanguageInteractor
 */
public class GetLanguageInteractorImpl extends AbstractInteractor implements GetLanguageInteractor {

    private final GetLanguageInteractor.Callback mCallback;
    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param callback           el callback en el que se notifican los resultados de las operaciones realizadas.
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public GetLanguageInteractorImpl(Executor executor, MainThread mainThread, Callback callback,
                                     SettingsRepository settingsRepository) {
        super(executor, mainThread);
        mCallback = callback;
        mSettingsRepository = settingsRepository;
    }

    @Override
    public void run() {
        final String language = checkDefault(mSettingsRepository.getLanguage());

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onLanguageRetrieved(language);
            }
        });
    }

    private String checkDefault(String language) {
        if (language == null || language.isEmpty()) {
            language = Locale.getDefault().toString();
            mSettingsRepository.saveLanguage(language);
        }
        return language;
    }
}
