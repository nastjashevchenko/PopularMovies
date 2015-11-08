package com.example.shevchenko.movies.Rest;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestClient {
    // TODO Singletones
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public ApiInterface service;

    public static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    public static ApiInterface getService() {
        return getRetrofit().create(ApiInterface.class);
    }
}
