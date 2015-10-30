package com.example.shevchenko.movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

 /**
 * Class for Movie object, store necessary information about Movie
 */
class Movie implements Parcelable {
    private final String id;
    private String mPosterPath;
    private String mTitle;
    private String mReleaseDate;
    private String mRating;
    private String mPlot;

    private static final String BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_SIZE = "w500";
    public static final String EXTRA_NAME = "movie_details";

    // JSON field names
    private static final String JSON_ID = "id";
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
    public Movie(JSONObject movieJson) throws JSONException {
        this.id = movieJson.getString(JSON_ID);
        this.mTitle = movieJson.getString(JSON_TITLE);
        this.mReleaseDate = movieJson.getString(JSON_RELEASE_DATE);
        this.mRating = movieJson.getString(JSON_RATING);
        this.mPlot = movieJson.getString(JSON_PLOT);
        setPosterPath(movieJson.getString(JSON_POSTER_RELATIVE_PATH), DEFAULT_SIZE);
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
        this.mPosterPath = BASE_PATH + size + relativePath;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getRating() {
        return mRating + "/10";
    }

    public String getPlot() {
        return mPlot;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(id);
         dest.writeString(mTitle);
         dest.writeString(mReleaseDate);
         dest.writeString(mRating);
         dest.writeString(mPlot);
         dest.writeString(mPosterPath);
     }

     public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
         public Movie createFromParcel(Parcel in) {
             return new Movie(in);
         }

         public Movie[] newArray(int size) {
             return new Movie[size];
         }
     };

     private Movie(Parcel in) {
         this.id = in.readString();
         this.mTitle = in.readString();
         this.mReleaseDate = in.readString();
         this.mRating = in.readString();
         this.mPlot = in.readString();
         this.mPosterPath = in.readString();
     }
}
