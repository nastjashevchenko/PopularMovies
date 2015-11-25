package com.example.shevchenko.movies.rest.ApiResponse;

import com.example.shevchenko.movies.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
/* Class to retrieve movies list from JSON to
/* Can not just use List<Movie> to call type due to JSON structure
 */
public class MoviesListApiResponse {
    @SerializedName("results")
    public ArrayList<Movie> movies;
}
