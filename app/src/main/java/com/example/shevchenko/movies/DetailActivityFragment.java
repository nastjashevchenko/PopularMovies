package com.example.shevchenko.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private static String id;
    private TextView mTitle;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mPlot;
    private ImageView mPoster;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        id = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mRating = (TextView) rootView.findViewById(R.id.rating);
        mReleaseDate = (TextView) rootView.findViewById(R.id.release);
        mPlot = (TextView) rootView.findViewById(R.id.plot);
        mPoster = (ImageView) rootView.findViewById(R.id.poster);

        GetMovieInfoTask getMovieInfo = new GetMovieInfoTask();
        getMovieInfo.execute(id);

        return rootView;
    }

    private class GetMovieInfoTask extends AsyncTask<String, Void, String> {
        private static final String LOG_TAG = "Movies, GetMoviesInfo";
        private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            Uri builder = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(id)
                    .build();
            return InternetUtil.getJsonResponseAsString(builder, LOG_TAG);
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null){
                try {
                    Movie movie = new Movie(id, response);
                    mTitle.setText(movie.getTitle());
                    mReleaseDate.setText(getResources().getString(R.string.release_date,
                            movie.getReleaseDate()));
                    mPlot.setText(movie.getPlot());
                    mRating.setText(getResources().getString(R.string.rating,
                            movie.getRating()));
                    Picasso.with(getActivity()).load(movie.getPosterPath())
                            .into(mPoster);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


