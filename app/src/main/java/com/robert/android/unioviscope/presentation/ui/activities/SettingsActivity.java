package com.robert.android.unioviscope.presentation.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.domain.executor.impl.ThreadExecutor;
import com.robert.android.unioviscope.presentation.presenters.SettingsPresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.SettingsPresenterImpl;
import com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity;
import com.robert.android.unioviscope.presentation.ui.utils.SnackbarUtil;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;
import com.robert.android.unioviscope.threading.MainThreadImpl;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemSelect;
import org.androidannotations.annotations.ViewById;

/**
 * Activity que extiende la clase AbstractActivity e implementa la interfaz del view del presenter SettingsPresenter.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.presentation.ui.activities.base.AbstractActivity
 * @see com.robert.android.unioviscope.presentation.presenters.SettingsPresenter.View
 */
@SuppressLint({"Registered", "NonConstantResourceId"})
@EActivity(R.layout.activity_settings)
public class SettingsActivity extends AbstractActivity implements SettingsPresenter.View {

    // vistas que se ocultan al realizar alguna operación (cargando)
    @ViewById(R.id.faceRecognitionLayout)
    View mFaceRecognitionLayout;
    @ViewById(R.id.firstDivider)
    View mFirstDivider;
    @ViewById(R.id.homeScreenLayout)
    View mHomeScreenLayout;
    @ViewById(R.id.secondDivider)
    View mSecondDivider;
    @ViewById(R.id.languageLayout)
    View mLanguageLayout;
    @ViewById(R.id.thirdDivider)
    View mThirdDivider;

    // vista del spinner (cargando)
    @ViewById(R.id.progressBarConstraintLayout)
    View mProgressBarLayout;

    @ViewById(R.id.lay_activity_settings)
    View mSettingsLayout;

    @ViewById
    Switch swFaceRecognition;
    @ViewById
    Switch swHomeScreen;
    @ViewById
    Spinner spLanguage;

    private SettingsPresenter mSettingsPresenter;
    private String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsPresenter = new SettingsPresenterImpl(ThreadExecutor.getInstance(), MainThreadImpl.getInstance(),
                this, getContext(), new SettingsRepositoryImpl(getContext()));
    }

    @Override
    protected void onResume() {
        mSettingsPresenter.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSettingsPresenter.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSettingsPresenter.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mSettingsPresenter.destroy();
        super.onDestroy();
    }

    @AfterViews
    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        setProgressBarLayout(mProgressBarLayout);
        addViewsToHide(mFaceRecognitionLayout, mFirstDivider, mHomeScreenLayout, mSecondDivider, mLanguageLayout,
                mThirdDivider);

        mSettingsPresenter.getFaceRecognition();
        mSettingsPresenter.getHomeScreen();
        mSettingsPresenter.getLanguage();
    }

    @CheckedChange(R.id.swFaceRecognition)
    public void facialRecognition(boolean isChecked) {
        mSettingsPresenter.saveFaceRecognition(isChecked);
    }

    @CheckedChange(R.id.swHomeScreen)
    public void homeScreen(boolean isChecked) {
        mSettingsPresenter.saveHomeScreen(isChecked);
    }

    @ItemSelect(R.id.spLanguage)
    public void language(@SuppressWarnings("UnusedParameters") boolean selected, int position) {
        if (mSettingsPresenter.saveLanguageByIndex(locale, position))
            recreate();
    }

    @Override
    public void onFaceRecognitionRetrieved(Boolean isFaceRecognition) {
        swFaceRecognition.setChecked(isFaceRecognition);
    }

    @Override
    public void onHomeScreenRetrieved(Boolean isHomeScreen) {
        swHomeScreen.setChecked(isHomeScreen);
    }

    @Override
    public void onLanguageRetrieved(String language) {
        locale = language;
        spLanguage.setAdapter(mSettingsPresenter.getLanguagesAdapter(locale));
        spLanguage.setSelection(mSettingsPresenter.getIndexByLocale(language));
    }

    @Override
    public void noInternetConnection() {
        SnackbarUtil.makeLong(mSettingsLayout, R.string.msg_no_internet_connection);
    }

    @Override
    public void serviceNotAvailable() {
        SnackbarUtil.makeLong(mSettingsLayout, R.string.msg_service_not_avabile);
    }
}
