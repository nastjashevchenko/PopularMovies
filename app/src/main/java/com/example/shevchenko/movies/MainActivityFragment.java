package com.example.shevchenko.movies;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shevchenko.movies.Adapters.ImageAdapter;
import com.example.shevchenko.movies.Model.Movie;
import com.example.shevchenko.movies.Rest.ApiResponse.MoviesListApiResponse;
import com.example.shevchenko.movies.Rest.RestClient;

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
    private List<Movie> mMoviesList;
    private static final String VOTE_VALUE = "100";

    public MainActivityFragment() {
    }

    private void createMoviesList(){
        if (InternetUtil.checkConnection(getContext())) {
            String sorting = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                    getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
            Call<MoviesListApiResponse> call = RestClient.getService().getMovies(sorting,
                    VOTE_VALUE, ApiKey.getApiKey());
            call.enqueue(new Callback<MoviesListApiResponse>() {
                @Override
                public void onResponse(Response<MoviesListApiResponse> response, Retrofit retrofit) {
                    MoviesListApiResponse moviesListResponse = response.body();
                    List<Movie> movies = moviesListResponse.movies;
                    if (movies != null){
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
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