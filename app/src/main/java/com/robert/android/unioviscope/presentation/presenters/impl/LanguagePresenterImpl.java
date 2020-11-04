package com.robert.android.unioviscope.presentation.presenters.impl;

import com.robert.android.unioviscope.domain.interactors.impl.GetLanguageSynchronousInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.presentation.presenters.LanguagePresenter;

/**
 * Clase que implementa la interfaz SessionPresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.LanguagePresenter
 */
public class LanguagePresenterImpl implements LanguagePresenter {

    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param settingsRepository el repositorio de las preferencias (ajustes de la aplicación) del estudiante.
     */
    public LanguagePresenterImpl(SettingsRepository settingsRepository) {
        mSettingsRepository = settingsRepository;
    }

    @Override
    public String getLanguage() {
        return new GetLanguageSynchronousInteractorImpl(mSettingsRepository).getLanguage();
    }
}
