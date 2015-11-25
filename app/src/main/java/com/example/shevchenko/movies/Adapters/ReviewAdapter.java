package com.example.shevchenko.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shevchenko.movies.model.Review;
import com.example.shevchenko.movies.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReviewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Review> mReviews;

    @Bind(R.id.author) TextView mAuthor;
    @Bind(R.id.review_text) TextView mText;

    public ReviewAdapter(Context mContext, List<Review> reviews) {
        this.mContext = mContext;
        this.mReviews = reviews;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return mReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        Review review = (Review) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.review_view, parent, false);
        }

        ButterKnife.bind(this, convertView);
        mAuthor.setText(review.getAuthor());
        // TODO make reviews collapsable-expandable
        mText.setText(review.getContent());
        return convertView;
    }
}