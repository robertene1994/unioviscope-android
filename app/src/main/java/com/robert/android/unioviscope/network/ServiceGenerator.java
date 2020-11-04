package com.robert.android.unioviscope.network;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.robert.android.unioviscope.BuildConfig;
import com.robert.android.unioviscope.network.converter.DateDeserializer;
import com.robert.android.unioviscope.network.converter.NullOrEmptyConverterFactory;
import com.robert.android.unioviscope.network.security.AuthenticationInterceptor;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Clase que genera los servicios de la API disponibles para los estudiantes.
 *
 * @author Robert Ene
 */
public class ServiceGenerator {

    private static final String API_BASE_URL = BuildConfig.UNIOVISCOPE_API;
    private static final Long CONNECT_TIMEOUT_SEC = 5L;
    private static final Long READ_TIMEOUT_SEC = 5L;
    private static final Long WRITE_TIMEOUT_SEC = 5L;

    private static Retrofit.Builder retrofitBuilder;

    /**
     * Método que crea un servicio determinado.
     *
     * @param <T>          el tipo del servicio que se crea.
     * @param context      el contexto de la aplicación.
     * @param serviceClass la clase del servicio que se crea.
     * @return el servicio creado.
     */
    public static <T> T createService(Context context, Class<T> serviceClass) {
        if (retrofitBuilder == null)
            setUp(context);
        return retrofitBuilder.build().create(serviceClass);
    }

    private static void setUp(Context context) {
        GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());

        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(context);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS);

        if (!httpClientBuilder.interceptors().contains(interceptor))
            httpClientBuilder.addInterceptor(interceptor);

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(new NullOrEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
    }
}
