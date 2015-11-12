package com.example.shevchenko.movies.Rest;


import com.example.shevchenko.movies.Rest.ApiResponse.MoviesListApiResponse;
import com.example.shevchenko.movies.Rest.ApiResponse.ReviewApiResponse;
import com.example.shevchenko.movies.Rest.ApiResponse.VideoApiResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {

    // TODO get rid of api_key in every query

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
