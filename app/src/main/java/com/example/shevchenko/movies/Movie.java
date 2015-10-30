package com.example.shevchenko.movies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

 /**
 * Class for Movie object, store necessary information about Movie
 */
class Movie implements Parcelable {
    private final String id;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private String posterPath;

    private static final String BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_SIZE = "w500";
    public static final String EXTRA_NAME = "movie_details";

    public Movie(String id) {
        this.id = id;
    }

     /**
      * Static method to get all necessary Movie fields from JSON response for this movie
      * from MovieDB API.
      */
    public static Movie parseFromJson(JSONObject movieJson) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        Movie movie = gson.fromJson(movieJson.toString(), Movie.class);
        movie.setPosterPath(movie.posterPath, DEFAULT_SIZE);
        return movie;
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

    public String getVoteAverage() {
        return voteAverage + "/10";
    }

    public String getOverview() {
        return overview;
    }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(id);
         dest.writeString(title);
         dest.writeString(releaseDate);
         dest.writeString(voteAverage);
         dest.writeString(overview);
         dest.writeString(posterPath);
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
         this.title = in.readString();
         this.releaseDate = in.readString();
         this.voteAverage = in.readString();
         this.overview = in.readString();
         this.posterPath = in.readString();
     }
}
