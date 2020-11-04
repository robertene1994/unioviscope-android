package com.robert.android.unioviscope.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.robert.android.unioviscope.domain.repository.SettingsRepository;

/**
 * Clase que implementa la interfaz SettingsRepository haciendo uso de las preferencias compartidas proporcionado por
 * el framework de Android.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.repository.SettingsRepository
 */
public class SettingsRepositoryImpl implements SettingsRepository {

    private static final String PREFS_NAME = "UniOviSCOPE";
    private static final String FACE_RECOGNITION = "FACE_RECOGNITION";
    private static final String HOME_SCREEN = "HOME_SCREEN";
    private static final String LANGUAGE = "LANGUAGE";

    private final SharedPreferences mSettings;
    private final SharedPreferences.Editor mEditor;

    /**
     * Contructor que instancia un nuevo repositorio.
     *
     * @param context el contexto de la aplicación.
     */
    @SuppressLint("CommitPrefEdits")
    public SettingsRepositoryImpl(Context context) {
        mSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    @Override
    public Boolean getFaceRecognition() {
        if (!mSettings.contains(FACE_RECOGNITION))
            return null;

        return mSettings.getBoolean(FACE_RECOGNITION, Boolean.TRUE);
    }

    @Override
    public void saveFaceRecognition(Boolean isFaceRecognition) {
        mEditor.putBoolean(FACE_RECOGNITION, isFaceRecognition);
        mEditor.commit();
    }

    @Override
    public Boolean getHomeScreen() {
        if (!mSettings.contains(HOME_SCREEN))
            return null;

        return mSettings.getBoolean(HOME_SCREEN, Boolean.FALSE);
    }

    @Override
    public void saveHomeScreen(Boolean isHomeScreen) {
        mEditor.putBoolean(HOME_SCREEN, isHomeScreen);
        mEditor.commit();
    }

    @Override
    public String getLanguage() {
        if (!mSettings.contains(LANGUAGE))
            return null;

        return mSettings.getString(LANGUAGE, "");
    }

    @Override
    public void saveLanguage(String language) {
        mEditor.putString(LANGUAGE, language);
        mEditor.commit();
    }
}
