package com.example.shevchenko.movies.model;

import android.net.Uri;

/**
 * Class for Video object, store necessary information about movie video
 * Objects are automatically created by API service, parsing JSON
 */
public class Video {
    private String id;
    private String key;
    private String site;
    private String type;
    private String name;

    private static final String YOUTUBE_URL = "http://youtube.com/watch";
    private static final String YOUTUBE_KEY = "v";

    // Didn't find any other sources except Youtube
    // If there are any, need to check source and create mapping for source and URI builder
    public Uri getUri() {
        return Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY, key)
                .build();
    }

    public String getName() {
        return name;
    }
}