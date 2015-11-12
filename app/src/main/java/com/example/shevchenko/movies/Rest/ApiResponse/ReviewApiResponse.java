package com.example.shevchenko.movies.Rest.ApiResponse;


import com.example.shevchenko.movies.Model.Review;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReviewApiResponse {
    @SerializedName("results")
    public ArrayList<Review> reviews;
}
