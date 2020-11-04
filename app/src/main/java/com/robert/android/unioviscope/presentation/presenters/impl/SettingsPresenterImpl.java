package com.robert.android.unioviscope.presentation.presenters.impl;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.Executor;
import com.robert.android.unioviscope.domain.executor.MainThread;
import com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor;
import com.robert.android.unioviscope.domain.interactors.GetHomeScreenInteractor;
import com.robert.android.unioviscope.domain.interactors.GetLanguageInteractor;
import com.robert.android.unioviscope.domain.interactors.SaveSettingsInteractor;
import com.robert.android.unioviscope.domain.interactors.impl.GetFaceRecognitionInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.GetHomeScreenInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.GetLanguageInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.SaveFaceRecognitionInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.SaveHomeScreenInteractorImpl;
import com.robert.android.unioviscope.domain.interactors.impl.SaveLanguageInteractorImpl;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.presentation.presenters.SettingsPresenter;
import com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter;
import com.robert.android.unioviscope.utils.ContextWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Clase que extiende la clase AbstractPresenter e implementa la interfaz SettingsPresenter. Implementa las interfaces
 * de los callbacks de los interactors GetFaceRecognitionInteractor, GetHomeScreenInteractor, GetLanguageInteractor y
 * SaveSettingsInteractor.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.presenters.base.AbstractPresenter
 * @see com.robert.android.unioviscope.presentation.presenters.SettingsPresenter
 * @see com.robert.android.unioviscope.domain.interactors.GetFaceRecognitionInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.GetHomeScreenInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.GetLanguageInteractor.Callback
 * @see com.robert.android.unioviscope.domain.interactors.SaveSettingsInteractor.Callback
 */
public class SettingsPresenterImpl extends AbstractPresenter implements SettingsPresenter,
        GetFaceRecognitionInteractor.Callback, GetHomeScreenInteractor.Callback, GetLanguageInteractor.Callback,
        SaveSettingsInteractor.Callback {

    private Context mContext;
    private SettingsPresenter.View mView;
    private final SettingsRepository mSettingsRepository;

    private GetLanguageInteractor mGetLanguageInteractor;

    private ArrayAdapter<CharSequence> languages;
    private List<String> locales;

    /**
     * Contructor que instancia un nuevo presenter.
     *
     * @param executor           el executor de hilos.
     * @param mainThread         el hilo principal de la aplicación.
     * @param view               el view en el que se notifica los resultados de las operaciones realizadas.
     * @param context            el contexto de la aplicación.
     * @param settingsRepository the settings repository
     */
    public SettingsPresenterImpl(Executor executor, MainThread mainThread, View view, Context context,
                                 SettingsRepository settingsRepository) {
        super(executor, mainThread);
        mView = view;
        mContext = context;
        mSettingsRepository = settingsRepository;
    }

    @Override
    public void resume() {
        // no aplicable
    }

    @Override
    public void pause() {
        // no aplicable
    }

    @Override
    public void stop() {
        destroy();
    }

    @Override
    public void destroy() {
        mView = null;
        mContext = null;
        mGetLanguageInteractor = null;
    }

    @Override
    public void onSavedSettings() {
        // no aplicable
    }

    @Override
    public void onFaceRecognitionRetrieved(Boolean isFaceRecognition) {
        mView.onFaceRecognitionRetrieved(isFaceRecognition);
        mView.hideProgress();
    }

    @Override
    public void onHomeScreenRetrieved(Boolean isHomeScreen) {
        mView.onHomeScreenRetrieved(isHomeScreen);
        mView.hideProgress();
    }

    @Override
    public void onLanguageRetrieved(String language) {
        mView.onLanguageRetrieved(language);
        mView.hideProgress();
    }

    @Override
    public SpinnerAdapter getLanguagesAdapter(String locale) {
        mContext = ContextWrapper.wrap(mContext, new Locale(locale));
        loadLanguagesAndLocales();
        return languages;
    }

    @Override
    public Integer getIndexByLocale(String locale) {
        return locales.indexOf(locale);
    }

    @Override
    public void getFaceRecognition() {
        mView.showProgress();
        new GetFaceRecognitionInteractorImpl(mExecutor, mMainThread, this, mSettingsRepository).execute();
    }

    @Override
    public void saveFaceRecognition(Boolean isFaceRecognition) {
        new SaveFaceRecognitionInteractorImpl(mExecutor, mMainThread,
                this, mSettingsRepository, isFaceRecognition).execute();
    }

    @Override
    public void getHomeScreen() {
        mView.showProgress();
        new GetHomeScreenInteractorImpl(mExecutor, mMainThread,this, mSettingsRepository).execute();
    }

    @Override
    public void saveHomeScreen(Boolean isHomeScreen) {
        new SaveHomeScreenInteractorImpl(mExecutor, mMainThread,
                this, mSettingsRepository, isHomeScreen).execute();
    }

    @Override
    public void getLanguage() {
        mView.showProgress();
        mGetLanguageInteractor = new GetLanguageInteractorImpl(mExecutor, mMainThread, this, mSettingsRepository);
        mGetLanguageInteractor.execute();
    }

    @Override
    public Boolean saveLanguageByIndex(String language, Integer index) {
        if (!locales.get(index).equals(language)) {
            String locale = locales.get(index);
            saveLanguage(locale);
            return true;
        }
        return false;
    }

    private void loadLanguagesAndLocales() {
        locales = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.pref_language_values)));
        languages = new ArrayAdapter<CharSequence>(mContext,R.layout.language_item,
                mContext.getResources().getStringArray(R.array.pref_language_titles));
        languages.setDropDownViewResource(R.layout.language_dropdown_item);
    }

    private void saveLanguage(String language) {
        new SaveLanguageInteractorImpl(mExecutor, mMainThread,
                this, mSettingsRepository, language).execute();
    }
}
