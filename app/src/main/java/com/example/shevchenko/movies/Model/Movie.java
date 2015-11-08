package com.example.shevchenko.movies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class for Movie object, store necessary information about Movie
 */
public class Movie implements Parcelable {
    private final String id;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private String posterPath;

    private static final String BASE_PATH = "http://image.tmdb.org/t/p/";
    public static final String DEFAULT_SIZE = "w500";
    public static final String DETAILS_SIZE = "w342";
    public static final String EXTRA_NAME = "movie_details";

    public Movie(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPosterPath(String size) {
        return BASE_PATH + size + posterPath;
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
