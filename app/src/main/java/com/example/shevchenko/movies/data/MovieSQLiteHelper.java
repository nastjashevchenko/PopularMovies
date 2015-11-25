package com.example.shevchenko.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shevchenko.movies.data.MovieContract.MovieEntry;

public class MovieSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String MOVIE_TABLE_CREATE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "(" +
            MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_RELEASE + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_RATING + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL" +
            ");";

    public MovieSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MOVIE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
