package com.robert.android.unioviscope.domain.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Clase de utilidades para conexión a Internet.
 *
 * @author Robert Ene
 */
public class ConnectivityStatus {

    /**
     * Método que devuelve el estado de la conexión a Internet del dispositivo.
     *
     * @param context el contexto de la aplicación.
     * @return el estado de la conexión a Internet del dispositivo.
     */
    @SuppressWarnings({"BooleanMethodIsAlwaysInverted", "ConstantConditions"})
    public static boolean isInternetConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        return connection != null && connection.isConnectedOrConnecting();
    }
}