package com.example.shevchenko.movies.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @Bind(R.id.movies_grid) GridView mGridView;
    @Bind(R.id.no_fav_movies_message) TextView mNoFavMovies;
    @Bind(R.id.cant_load_movies) LinearLayout mNoOnlineMovies;
    @Bind(R.id.try_again) Button mTryAgain;

    private List<Movie> mMoviesList = new ArrayList<>();
    private MovieCursorAdapter favAdapter;
    private ImageAdapter imageAdapter;
    private boolean favourite;
    private int mPosition;
    private String mSorting;
    private static final String VOTE_VALUE = "50";
    private static final String POSITION_KEY = "Position";
    private static final String MOVIES_LIST_KEY = "MoviesList";

    public MainActivityFragment() {
    }

    private boolean updateSorting() {
        String sorting = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
        // If mSorting is not initialized yet, do not consider it was changed
        favourite = (sorting.equals(getString(R.string.favourite)));
        boolean isChanged = (mSorting != null && !mSorting.equals(sorting));
        mSorting = sorting;
        return isChanged;
    }

    private void showCantLoadMoviesBlock() {
        mNoOnlineMovies.setVisibility(View.VISIBLE);
        mMoviesList = new ArrayList<>();
        mGridView.setAdapter(null);
    }

    private void createOnlineMoviesList(String sorting){
        if (! InternetUtil.checkConnection(getContext())) {
            // Should not even try sending requests if there is no connection
            showCantLoadMoviesBlock();
            return;
        }
        Call<MoviesListApiResponse> call = RestClient.getService().getMovies(sorting,
                VOTE_VALUE, ApiKey.getApiKey());
        call.enqueue(new Callback<MoviesListApiResponse>() {
            @Override
            public void onResponse(Response<MoviesListApiResponse> response, Retrofit retrofit) {
                MoviesListApiResponse moviesListResponse = response.body();
                if (moviesListResponse != null) {
                    mMoviesList = moviesListResponse.movies;
                    imageAdapter = new ImageAdapter(getActivity(), mMoviesList);
                    mGridView.setAdapter(imageAdapter);
                    if (mPosition != -1) mGridView.smoothScrollToPosition(mPosition);
                } else {
                    showCantLoadMoviesBlock();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showCantLoadMoviesBlock();
            }
        });
    }

    private LoaderManager.LoaderCallbacks<Cursor> favoriteLoader =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
                    // If no fav moves in DB, simply show message about it
                    if (data.getCount() == 0) {
                        mNoFavMovies.setVisibility(View.VISIBLE);
                        return;
                    }
                    // Update mMoviesList from Cursor
                    mMoviesList = new ArrayList<>();
                    while (data.moveToNext()) {
                        mMoviesList.add(new Movie(data));
                    }
                    if (mPosition != -1) mGridView.smoothScrollToPosition(mPosition);
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
        // Before updating, hide all messages for empty movie list and show it depending on
        // createMovieList results
        mNoFavMovies.setVisibility(View.GONE);
        mNoOnlineMovies.setVisibility(View.GONE);

        if (favourite) {
            createFavMoviesList();
        } else {
            createOnlineMoviesList(mSorting);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != -1) {
            outState.putInt(POSITION_KEY, mPosition);
        }
        if (mMoviesList != null && !favourite) {
            outState.putParcelableArrayList(MOVIES_LIST_KEY, (ArrayList<? extends Parcelable>) mMoviesList);
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
        // Need to update list onResume only if sorting type was changed or type is Favourite
        // Need to update Favourites every time in case user unliked movie from detailed view
        if (updateSorting() || favourite) {
            createMoviesList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        // Call updateSorting to init favourite and sorting depending on current setting
        updateSorting();

        // Restore from savedInstance if we have anything in Bundle, else load list online or from DB
        if (!favourite && savedInstanceState != null && savedInstanceState.containsKey(MOVIES_LIST_KEY)) {
            mMoviesList = (List<Movie>)savedInstanceState.get(MOVIES_LIST_KEY);
            imageAdapter = new ImageAdapter(getActivity(), mMoviesList);
            mGridView.setAdapter(imageAdapter);
        } else {
            createMoviesList();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(POSITION_KEY);
        }

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mPosition = position;
                Movie movie = mMoviesList.get(position);
                ((ItemClickCallback) getActivity()).onItemClick(movie, position);
            }
        });

        // If was not able to load movies, can re-try by clicking Try Again button
        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMoviesList();
            }
        });
        return view;
    }

    interface ItemClickCallback {
        void onItemClick(Movie movie, int position);
    }
}