package com.example.shevchenko.movies.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shevchenko.movies.ApiKey;
import com.example.shevchenko.movies.InternetUtil;
import com.example.shevchenko.movies.R;
import com.example.shevchenko.movies.adapters.ImageAdapter;
import com.example.shevchenko.movies.adapters.MovieCursorAdapter;
import com.example.shevchenko.movies.data.MovieContract;
import com.example.shevchenko.movies.model.Movie;
import com.example.shevchenko.movies.rest.ApiResponse.MoviesListApiResponse;
import com.example.shevchenko.movies.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    // Don't really think that there is sense in binding when there is only one view
    // Keeping it for consistency with DetailActivityFragment
    @Bind(R.id.movies_grid) GridView mGridView;
    private List<Movie> mMoviesList = new ArrayList<>();
    private MovieCursorAdapter favAdapter;
    private String sorting;
    private boolean favourite = false; //by default sorting is by popularity
    private static final String VOTE_VALUE = "100";

    public MainActivityFragment() {
    }

    private String getSorting() {
        String sorting = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
        favourite = (sorting.equals(getString(R.string.favourite)));
        return sorting;
    }
    private void createOnlineMoviesList(String sorting){
        if (InternetUtil.checkConnection(getContext())) {
            Call<MoviesListApiResponse> call = RestClient.getService().getMovies(sorting,
                    VOTE_VALUE, ApiKey.getApiKey());
            call.enqueue(new Callback<MoviesListApiResponse>() {
                @Override
                public void onResponse(Response<MoviesListApiResponse> response, Retrofit retrofit) {
                    MoviesListApiResponse moviesListResponse = response.body();
                    // TODO if response is null, but internet connection true and response was successful
                    if (moviesListResponse != null){
                        List<Movie> movies = moviesListResponse.movies;
                        List<String> imageUrls = new ArrayList<>();
                        for (Movie movie : movies) {
                            imageUrls.add(movie.getPosterPath(Movie.DEFAULT_SIZE));
                        }
                        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), imageUrls);
                        mGridView.setAdapter(imageAdapter);
                        mMoviesList = movies;
                    }
                }
                @Override
                public void onFailure(Throwable t) {
                    // TODO test with no connection
                    Toast toast = Toast.makeText(getContext(), R.string.cant_load_movies,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } else {
            // TODO: do I need this after setting up retrofit callback?
            // TODO: show alert message in Fragment, not Toast
            // TODO: if get back from detail view, do not show message
            Toast toast = Toast.makeText(getContext(), R.string.no_connection_alert,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> favoriteLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    // Return the loader for use
                    return new CursorLoader(getActivity(),
                            MovieContract.MovieEntry.CONTENT_URI, // URI
                            null, // projection fields
                            null, // the selection criteria
                            null, // the selection args
                            null // the sort order
                    );
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    favAdapter.swapCursor(data);
                    if (mMoviesList.size() > 0) {
                        mMoviesList.clear();
                    }
                    while (data.moveToNext()) {
                        mMoviesList.add(new Movie(data));
                    }
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {
                    favAdapter.swapCursor(null);
                }
            };

    private void createFavMoviesList() {
        favAdapter = new MovieCursorAdapter(getContext(), null);
        getActivity().getSupportLoaderManager().restartLoader(1, new Bundle(), favoriteLoader);

        mGridView.setAdapter(favAdapter);
        favAdapter.notifyDataSetChanged();
    }

    private void createMoviesList() {
        mMoviesList.clear();
        sorting = getSorting();
        if (favourite) {
            createFavMoviesList();
        } else {
            createOnlineMoviesList(sorting);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO save state in bundle
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        createMoviesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        createMoviesList();

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movie = mMoviesList.get(position);
                Intent movieDetails = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Movie.EXTRA_NAME, movie);
                startActivity(movieDetails);
            }
        });
        return view;
    }
}