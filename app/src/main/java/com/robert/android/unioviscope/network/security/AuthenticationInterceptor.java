package com.robert.android.unioviscope.network.security;

import android.content.Context;

import com.robert.android.unioviscope.domain.repository.SessionRepository;
import com.robert.android.unioviscope.domain.repository.SettingsRepository;
import com.robert.android.unioviscope.storage.SessionRepositoryImpl;
import com.robert.android.unioviscope.storage.SettingsRepositoryImpl;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Clase que implementa la interfaz Interceptor y que se encarga de añadir las cabeceras necesarias a las peticiones
 * realizadas por el dispositivo del estudiante.
 *
 * @author Robert Ene
 * @see okhttp3.Interceptor
 */
public class AuthenticationInterceptor implements Interceptor {

    private final SessionRepository mSessionRepository;
    private final SettingsRepository mSettingsRepository;

    /**
     * Contructor que instancia un nuevo interceptor.
     *
     * @param context el contexto de la aplicación.
     */
    public AuthenticationInterceptor(Context context) {
        mSessionRepository = new SessionRepositoryImpl(context);
        mSettingsRepository = new SettingsRepositoryImpl(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = mSessionRepository.getToken();
        String language = mSettingsRepository.getLanguage();
        Request.Builder builder = chain.request().newBuilder();

        if (token != null)
            builder = builder.addHeader("Authorization", token);

        if (language != null)
            builder = builder.addHeader("Accept-Language", language);

        return chain.proceed(builder.build());
    }
}
