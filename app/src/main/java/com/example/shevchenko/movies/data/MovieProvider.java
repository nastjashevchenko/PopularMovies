package com.example.shevchenko.movies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;


public class MovieProvider extends ContentProvider {
    private MovieSQLiteHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
         return mDbHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return MovieContract.MovieEntry.CONTENT_TYPE;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri returnUri;
        long _id = mDbHelper.getWritableDatabase().insert(
                MovieContract.MovieEntry.TABLE_NAME, null, values);
        if ( _id > 0 )
            returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
        else
            throw new android.database.SQLException("Failed to insert row into " + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if ( null == selection ) selection = "1";

        int rowsDeleted = mDbHelper.getWritableDatabase().delete(
                MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = mDbHelper.getWritableDatabase().update(
                MovieContract.MovieEntry.TABLE_NAME, values, selection,
                selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
