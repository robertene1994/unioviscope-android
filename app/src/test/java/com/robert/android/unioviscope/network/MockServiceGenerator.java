package com.robert.android.unioviscope.network;

import com.google.gson.GsonBuilder;
import com.robert.android.unioviscope.BuildConfig;
import com.robert.android.unioviscope.network.converter.DateDeserializer;
import com.robert.android.unioviscope.network.converter.NullOrEmptyConverterFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Clase que genera mocks para los servicios de la API disponibles para los estudiantes.
 *
 * @author Robert Ene
 */
public class MockServiceGenerator {

    private static final String API_BASE_URL_TEST = BuildConfig.UNIOVISCOPE_API_TEST;
    private static final Long CONNECT_TIMEOUT_SEC = 5L;
    private static final Long READ_TIMEOUT_SEC = 5L;
    private static final Long WRITE_TIMEOUT_SEC = 5L;

    private static Retrofit.Builder retrofitBuilder;

    /**
     * Método que crea un mock para un servicio determinado.
     *
     * @param <T>           el tipo del servicio que se crea.
     * @param mockWebServer el servidor que sirve de mock para las peticiones realizadas.
     * @param serviceClass  la clase del servicio que se crea.
     * @return el mock del servicio creado.
     */
    public static <T> T createService(MockWebServer mockWebServer, Class<T> serviceClass) {
        if (retrofitBuilder == null)
            setUp(mockWebServer);
        return retrofitBuilder.build().create(serviceClass);
    }

    public static void setUp(MockWebServer mockWebServer) {
        GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS);

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(mockWebServer.url(API_BASE_URL_TEST))
                .client(httpClientBuilder.build())
                .addConverterFactory(new NullOrEmptyConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));
    }
}
