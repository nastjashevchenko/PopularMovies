package com.example.shevchenko.movies.rest;


import com.example.shevchenko.movies.rest.ApiResponse.MoviesListApiResponse;
import com.example.shevchenko.movies.rest.ApiResponse.ReviewApiResponse;
import com.example.shevchenko.movies.rest.ApiResponse.VideoApiResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {
    @GET("discover/movie")
    Call<MoviesListApiResponse> getMovies(
            @Query("sort_by") String sort,
            @Query("vote_count.gte") String min_votes,
            @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<VideoApiResponse> getVideos(
            @Path("id") String id,
            @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewApiResponse> getReviews(
            @Path("id") String id,
            @Query("api_key") String api_key);
}
