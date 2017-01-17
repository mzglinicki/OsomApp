package com.mzglinicki.ossomapp.webService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Marcin on 16.01.2017.
 */

public class RestService {

    private static final String BASE_URL = "http://conf.osomapps.com/";
    private static final int TIMEOUT = 30;
    private final WebService webService;

    public RestService() {

        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        webService = retrofit.create(WebService.class);
    }

    public WebService getWebService() {
        return webService;
    }
}
