package com.example.shevchenko.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Movie mMovie = getActivity().getIntent().getParcelableExtra(Movie.EXTRA_NAME);
        TextView mTitle = (TextView) rootView.findViewById(R.id.title);
        TextView mRating = (TextView) rootView.findViewById(R.id.rating);
        TextView mReleaseDate = (TextView) rootView.findViewById(R.id.release);
        TextView mPlot = (TextView) rootView.findViewById(R.id.plot);
        ImageView mPoster = (ImageView) rootView.findViewById(R.id.poster);

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


