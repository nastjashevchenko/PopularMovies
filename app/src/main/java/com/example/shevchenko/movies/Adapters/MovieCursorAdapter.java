package com.example.shevchenko.movies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shevchenko.movies.R;
import com.example.shevchenko.movies.data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ImageView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView image = (ImageView) view;
        // I am using Picasso and URL to show posters for fav movies, which will work offline only
        // if image is in cache and will show placeholder otherwise.
        // Ideally, when adding movie to favs, I should download image to disk and save path to it
        // in DB, then load poster from device storage.
        Picasso.with(mContext)
                .load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)))
                .placeholder(R.drawable.placeholder)
                .into(image);
    }
}
