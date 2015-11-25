package com.example.shevchenko.movies.rest.ApiResponse;


import com.example.shevchenko.movies.model.Review;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewApiResponse {
    @SerializedName("results")
    public ArrayList<Review> reviews;
}
