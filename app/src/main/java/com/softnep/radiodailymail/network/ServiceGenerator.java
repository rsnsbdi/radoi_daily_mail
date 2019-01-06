package com.softnep.radiodailymail.network;


import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ADMIN on 2017-11-07.
 */

public class ServiceGenerator {

    public static final String API = "https://api.radiodailymail.com/3459IaYYku12qPq792S/";

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createRequestGsonAPI(Class<S> serviceClass) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                 .writeTimeout(200, TimeUnit.SECONDS)
                 .readTimeout(200, TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                                .create()
                ))
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

}
