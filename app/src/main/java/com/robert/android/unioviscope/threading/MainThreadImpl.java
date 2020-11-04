package com.robert.android.unioviscope.threading;

import android.os.Handler;
import android.os.Looper;

import com.robert.android.unioviscope.domain.executor.MainThread;

/**
 * Clase que implementa la interfaz MainThread. Su objetivo es que las operaciones de los resultados de los
 * interactors se ejecuten en el hilo principal de la aplicación.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.executor.MainThread
 */
public class MainThreadImpl implements MainThread {

    private static MainThread sMainThread;
    private final Handler mHandler;

    private MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Método que devuelve una instancia del manejador de hilos.
     *
     * @return la instancia del manejador de hilos.
     */
    public static MainThread getInstance() {
        if (sMainThread == null)
            sMainThread = new MainThreadImpl();
        return sMainThread;
    }
}
