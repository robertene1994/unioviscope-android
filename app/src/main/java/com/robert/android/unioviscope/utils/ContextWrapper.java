package com.robert.android.unioviscope.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Clase que envuelve el contexto de la aplicación actualizando el idioma en función de las preferencias del usuario.
 *
 * @author Robert Ene
 */
public class ContextWrapper extends android.content.ContextWrapper {

    private ContextWrapper(Context base) {
        super(base);
    }

    /**
     * Método que envuelve el contexto de la aplicación actualizando el idioma en función de las preferencias del
     * usuario (o por defecto si el usuario no ha establecido preferencias).
     *
     * @param context el contexto de la aplicación.
     * @param locale  el objeto locale que refleja el idioma que se debe aplicar al contexto.
     * @return el nuevo contexto envuelto y actualizado según el idioma establecido.
     */
    public static ContextWrapper wrap(Context context, Locale locale) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        context = context.createConfigurationContext(configuration);
        return new ContextWrapper(context);
    }
}