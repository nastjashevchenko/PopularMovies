package com.example.shevchenko.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private GridView mGridView;
    private List<Movie> mMoviesList;

    public MainActivityFragment() {
    }

    private void createMoviesList(){
        GetMoviesInfoTask getMovies = new GetMoviesInfoTask();
        String sorting = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
        getMovies.execute(sorting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        createMoviesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) view.findViewById(R.id.movies_grid);
        createMoviesList();
        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent movieDetails = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, mMoviesList.get(position).getId());
                startActivity(movieDetails);
            }
        });
        return view;
    }

    private class GetMoviesInfoTask extends AsyncTask<String, Void, List<Movie>> {
        private static final String LOG_TAG = "Movies, GetMoviesInfo";
        private static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        private static final String SORT_PARAM = "sort_by";
        private static final String VOTE_COUNT = "vote_count.gte";
        private static final String VOTE_VALUE = "100";

        private List<Movie> parseResponse(String response) throws JSONException {
            List<Movie> popularMovies = new ArrayList<>();
            JSONArray jsonResponse = new JSONObject(response).getJSONArray("results");
            for (int i = 0; i < jsonResponse.length(); i++) {
                Movie movie = new Movie(jsonResponse.getJSONObject(i).getString("id"));
                movie.setPosterPath(jsonResponse.getJSONObject(i).getString("poster_path"),
                        Movie.DEFAULT_SIZE);
                popularMovies.add(movie);
            }
            return popularMovies;
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            String sort_type = params[0];
            Uri builder = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, sort_type)
                    // Return only movies with more than VOTE_VALUE number of votes
                    // Otherwise, movie with the highest rating can be one with 10/10 rating and 1 vote
                    .appendQueryParameter(VOTE_COUNT, VOTE_VALUE)
                    .build();
            try {
                return parseResponse(InternetUtil.getJsonResponseAsString(builder, LOG_TAG));
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null){
                List<String> imageUrls = new ArrayList<>();
                for (Movie movie : movies) {
                    imageUrls.add(movie.getPosterPath());
                }
                ImageAdapter imageAdapter = new ImageAdapter(getActivity(), imageUrls);
                mGridView.setAdapter(imageAdapter);
                mMoviesList = movies;
            }
        }
    }
}