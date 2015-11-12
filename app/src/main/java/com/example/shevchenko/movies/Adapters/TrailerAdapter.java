package com.example.shevchenko.movies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shevchenko.movies.Model.Video;
import com.example.shevchenko.movies.R;

import java.util.List;

import butterknife.Bind;

public class TrailerAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Video> mTrailers;

    @Bind(R.id.trailer_name) TextView mTrailerName;

    public TrailerAdapter(Context mContext, List<Video> mTrailers) {
        this.mContext = mContext;
        this.mTrailers = mTrailers;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Video trailer = (Video) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_view, parent, false);
        }

        mTrailerName.setText(trailer.getName());
        return convertView;
    }
}
