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
        Picasso.with(mContext)
                .load(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)))
                .placeholder(R.drawable.placeholder)
                .into(image);
    }
}
