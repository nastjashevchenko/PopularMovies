package com.example.shevchenko.movies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.shevchenko.movies.R;
import com.example.shevchenko.movies.model.Movie;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.ItemClickCallback{
    private boolean mTwoPane;
    @Bind(R.id.movie_detail_container) FrameLayout mDetailContainer;
    private static final String DETAILFRAGMENT_TAG = "DETAIL_FRAGMENT";
    private static final String SELECTED_TAG = "isMovieSelected";
    private String sorting;
    private boolean isMovieSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        ButterKnife.bind(this);
        sorting = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_TAG)) {
            isMovieSelected = savedInstanceState.getBoolean(SELECTED_TAG);
        }

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            // If no movie was clocked to show details or setting was changed and list updated
            // Detail fragment container should not be visible
            if (!isMovieSelected) {
                mDetailContainer.setVisibility(View.GONE);
            }

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PrefActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SELECTED_TAG, isMovieSelected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentSorting = PreferenceManager.getDefaultSharedPreferences(this).getString(
                getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default));
        if (!sorting.equals(currentSorting)) {
            mDetailContainer.setVisibility(View.GONE);
            sorting = currentSorting;
            isMovieSelected = false;
        }
    }

    @Override
    public void onItemClick(Movie movie, int position) {
        isMovieSelected = true;
        if (mTwoPane) {
            mDetailContainer.setVisibility(View.VISIBLE);

            Bundle args = new Bundle();
            args.putParcelable(Movie.EXTRA_NAME, movie);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent movieDetails = new Intent(this, DetailActivity.class)
                    .putExtra(Movie.EXTRA_NAME, movie);
            startActivity(movieDetails);
        }
    }
}
