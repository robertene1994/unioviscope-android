package com.robert.android.unioviscope.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.robert.android.unioviscope.domain.model.User;
import com.robert.android.unioviscope.domain.repository.SessionRepository;

/**
 * Clase que implementa la interfaz SessionRepository haciendo uso de las preferencias compartidas proporcionado por
 * el framework de Android.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.repository.SessionRepository
 */
public class SessionRepositoryImpl implements SessionRepository {

    private static final String PREFS_NAME = "UniOviSCOPE";
    private static final String TOKEN = "TOKEN";
    private static final String USER = "USER";

    private final SharedPreferences mSettings;
    private final SharedPreferences.Editor mEditor;
    private final Gson mGson;

    /**
     * Contructor que instancia un nuevo repositorio.
     *
     * @param context el contexto de la aplicación.
     */
    @SuppressLint("CommitPrefEdits")
    public SessionRepositoryImpl(Context context) {
        mSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
        mGson = new Gson();
    }

    @Override
    public User getUser() {
        String json = mSettings.getString(USER, "");
        return mGson.fromJson(json, User.class);
    }

    @Override
    public void saveUser(User user) {
        String json = mGson.toJson(user);
        mEditor.putString(USER, json);
        mEditor.commit();
    }

    @Override
    public void deleteUser() {
        mEditor.remove(USER);
        mEditor.commit();
    }

    @Override
    public String getToken() {
        return mSettings.getString(TOKEN, null);
    }

    @Override
    public void saveToken(String token) {
        mEditor.putString(TOKEN, token);
        mEditor.commit();
    }

    @Override
    public void deleteToken() {
        mEditor.remove(TOKEN);
        mEditor.commit();
    }
}
