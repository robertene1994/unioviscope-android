package com.robert.android.unioviscope.utils;

import android.content.Context;
import android.content.res.Configuration;

import com.robert.android.unioviscope.presentation.presenters.LanguagePresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.LanguagePresenterImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;

import java.util.Locale;

/**
 * Clase de utilidades para el idioma.
 *
 * @author Robert Ene
 */
public class LocaleUtil {

    /**
     * Método que devuelve un string en función del idioma establecido por el usuario.
     *
     * @param context    el contexto de la aplicación.
     * @param resourceId el id del recurso del string.
     * @return el string asociado al idioma establecido.
     */
    public static String getStringByLocale(Context context, int resourceId) {
        LanguagePresenter languagePresenter = new LanguagePresenterImpl(new SettingsRepositoryImpl(context));
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(languagePresenter.getLanguage()));
        return context.createConfigurationContext(configuration).getResources().getString(resourceId);
    }
}
