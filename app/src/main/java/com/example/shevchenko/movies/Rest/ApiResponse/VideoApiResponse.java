package com.example.shevchenko.movies.rest.ApiResponse;

import com.example.shevchenko.movies.model.Video;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Class to retrieve movies video list from JSON to
 */
public class VideoApiResponse {
    @SerializedName("results")
    public ArrayList<Video> videos;
}
