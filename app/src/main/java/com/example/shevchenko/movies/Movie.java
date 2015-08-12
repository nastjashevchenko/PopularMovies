package com.example.shevchenko.movies;

import org.json.JSONException;
import org.json.JSONObject;

 /**
 * Class for Movie object, store necessary information about Movie
 */
public class Movie {
    private String id;
    private String posterPath;
    private String title;
    private String releaseDate;
    private String rating;
    private String plot;
    private static final String BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_SIZE = "w500";
    private static final String SIZE_DETAILS_SCREEN = "w342";

    // JSON field names
    private static final String JSON_TITLE = "title";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_RATING = "vote_average";
    private static final String JSON_PLOT = "overview";
    private static final String JSON_POSTER_RELATIVE_PATH = "poster_path";

    public Movie(String id) {
        this.id = id;
    }

     /**
      * Constructor to get all necessary Movie fields from JSON response for this movie
      * from MovieDB API. Id also can be received from JSON, but we already know it, when
      * sending request.
      */
    public Movie(String id, String json) throws JSONException {
        this.id = id;
        JSONObject movieJson = new JSONObject(json);
        this.title = movieJson.getString(JSON_TITLE);
        this.releaseDate = movieJson.getString(JSON_RELEASE_DATE);
        this.rating = movieJson.getString(JSON_RATING);
        this.plot = movieJson.getString(JSON_PLOT);
        setPosterPath(movieJson.getString(JSON_POSTER_RELATIVE_PATH), SIZE_DETAILS_SCREEN);
    }

    public String getId() {
        return id;
    }

     /**
      * Method to set full URL to poster image.
      * Full URL consists from 3 parts: main (base) URL, image size and
      * relative path, which can be got from JSON response
      */
    public void setPosterPath(String relativePath, String size) {
        this.posterPath = BASE_PATH + size + relativePath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating + "/10";
    }

    public String getPlot() {
        return plot;
    }
}
