package com.example.shevchenko.movies.rest;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .build();

    private static final ApiInterface MOVIE_API_SERVICE = RETROFIT.create(ApiInterface.class);

    public static ApiInterface getService() {
        return MOVIE_API_SERVICE;
    }
}
