package com.example.shevchenko.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.rating) TextView mRating;
    @Bind(R.id.release) TextView mReleaseDate;
    @Bind(R.id.plot) TextView mPlot;
    @Bind(R.id.poster) ImageView mPoster;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Movie mMovie = getActivity().getIntent().getParcelableExtra(Movie.EXTRA_NAME);

        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(getResources().getString(R.string.release_date,
                mMovie.getReleaseDate()));
        mPlot.setText(mMovie.getPlot());
        mRating.setText(getResources().getString(R.string.rating,
                mMovie.getRating()));
        Picasso.with(getActivity()).load(mMovie.getPosterPath())
                .into(mPoster);

        return rootView;
    }
}


