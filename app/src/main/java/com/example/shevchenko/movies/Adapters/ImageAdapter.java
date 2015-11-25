package com.example.shevchenko.movies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shevchenko.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<String> mImageUrls;

    public ImageAdapter(Context c, List<String> urls) {
        mContext = c;
        mImageUrls = urls;
    }
    public int getCount() {
        return mImageUrls.size();
    }

    public String getItem(int position) {
        return null;
    }

    private String getItemUrl(int position) {
        return mImageUrls.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext)
                .load(getItemUrl(position))
                .placeholder(R.drawable.placeholder)
                .into(imageView);
        return imageView;
    }
}