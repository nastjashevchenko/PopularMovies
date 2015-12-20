package com.example.shevchenko.movies.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shevchenko.movies.data.MovieContract;

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
    public static final String EXTRA_NAME = "movie_details";

    public Movie(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getPosterPath() {
        return BASE_PATH + DEFAULT_SIZE + posterPath;
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

    public Movie(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
        this.title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
        this.releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE));
        this.voteAverage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
        this.overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
        this.posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
    }

    public boolean isFavorite(Context c) {
        Cursor cursor = c.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_TITLE},
                MovieContract.MovieEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null
        );
        boolean isFavourite = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return isFavourite;
    }

    public void deleteFavorite(Context c) {
        c.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.COLUMN_ID + " = ?",
                new String[]{id});
    }

    public void addFavorite(Context c) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_ID, id);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE, releaseDate);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RATING, voteAverage);
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, getPosterPath());

        c.getContentResolver().insert(
                MovieContract.MovieEntry.CONTENT_URI,
                movieValues);
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
