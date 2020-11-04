package com.robert.android.unioviscope.domain.interactors.impl;

import com.robert.android.unioviscope.domain.interactors.GetLanguageSynchronousInteractor;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;

import java.util.Locale;

/**
 * Clase que implementa la interfaz GetLanguageSynchronousInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.interactors.GetLanguageSynchronousInteractor
 */
public class GetLanguageSynchronousInteractorImpl implements GetLanguageSynchronousInteractor {

    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo interactor.
     *
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public GetLanguageSynchronousInteractorImpl(SettingsRepository settingsRepository) {
        mSettingsRepository = settingsRepository;
    }

    @Override
    public String getLanguage() {
        return checkDefault(mSettingsRepository.getLanguage());
    }

    private String checkDefault(String language) {
        if (language == null || language.isEmpty()) {
            language = Locale.getDefault().toString();
            mSettingsRepository.saveLanguage(language);
        }
        return language;
    }
}
