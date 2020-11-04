package com.robert.android.unioviscope;

import android.app.Application;
import android.content.Context;

import com.robert.android.unioviscope.presentation.presenters.LanguagePresenter;
import com.robert.android.unioviscope.presentation.presenters.impl.LanguagePresenterImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;
import com.robert.android.unioviscope.utils.ContextWrapper;

import java.io.File;
import java.util.Locale;

/**
 * Punto de entrada para la aplicación UniOviSCOPE.
 *
 * @author Robert Ene
 */
public class UniOviScopeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        wrapContextLanguage(getApplicationContext());
        deleteCache(getApplicationContext());
    }

    private void wrapContextLanguage(Context context) {
        LanguagePresenter languagePresenter = new LanguagePresenterImpl(new SettingsRepositoryImpl(context));
        String language = languagePresenter.getLanguage();
        ContextWrapper.wrap(context, new Locale(language));
    }

    private static void deleteCache(Context context) {
        File dir = context.getCacheDir();
        deleteDir(dir);
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                if (!deleteDir(new File(dir, child))) {
                    return false;
                }
            }
            return dir.delete();
        } else {
            return dir != null && dir.isFile() && dir.delete();
        }
    }
}
