package com.robert.android.unioviscope.utilTest.threading;

import com.robert.android.unioviscope.domain.executor.MainThread;

/**
 * Clase que implementa la interfaz MainThread. Se utiliza para las pruebas.
 *
 * @author Robert Ene
 * @see com.robert.android.unioviscope.domain.executor.MainThread
 */
public class TestMainThread implements MainThread {

    @Override
    public void post(Runnable runnable) {
        runnable.run();
    }
}
