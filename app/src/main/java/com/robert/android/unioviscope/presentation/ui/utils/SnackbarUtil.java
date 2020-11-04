package com.robert.android.unioviscope.presentation.ui.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.robert.android.unioviscope.R;
import com.robert.android.unioviscope.utils.LocaleUtil;

/**
 * Clase de utilidades para el Snackbar (mensajes en la pantalla).
 *
 * @author Robert Ene
 */
public class SnackbarUtil {

    /**
     * Método que muestra en la pantalla un mensaje de corta duración.
     *
     * @param view       la vista en la que se muestra el mensaje.
     * @param resourceId el id del mensaje que se muestra.
     */
    public static void makeShort(View view, int resourceId) {
        make(view, resourceId, Snackbar.LENGTH_SHORT);
    }

    /**
     * Método que muestra en la pantalla un mensaje de larga duración.
     *
     * @param view       la vista en la que se muestra el mensaje.
     * @param resourceId el id del mensaje que se muestra.
     */
    public static void makeLong(View view, int resourceId) {
        make(view, resourceId, Snackbar.LENGTH_LONG);
    }

    private static void make(View view, int resourceId, int duration) {
        Snackbar snackbar = Snackbar.make(view, LocaleUtil.getStringByLocale(view.getContext(), resourceId), duration);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.colorSecondaryText));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setMaxLines(3);
        snackbar.show();
    }
}
