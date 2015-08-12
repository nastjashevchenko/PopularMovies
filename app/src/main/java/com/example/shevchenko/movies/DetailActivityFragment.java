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
    private Movie movie;
    private String id;
    private TextView title;
    private TextView rating;
    private TextView releaseDate;
    private TextView plot;
    private ImageView poster;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        id = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);

        title = (TextView) rootView.findViewById(R.id.title);
        rating = (TextView) rootView.findViewById(R.id.rating);
        releaseDate = (TextView) rootView.findViewById(R.id.release);
        plot = (TextView) rootView.findViewById(R.id.plot);
        poster = (ImageView) rootView.findViewById(R.id.poster);

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
                    movie = new Movie(id, response);
                    title.setText(movie.getTitle());
                    releaseDate.setText("Release date: " + movie.getReleaseDate());
                    plot.setText(movie.getPlot());
                    rating.setText("Rating: " + movie.getRating());
                    Picasso.with(getActivity()).load(movie.getPosterPath())
                            .into(poster);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


