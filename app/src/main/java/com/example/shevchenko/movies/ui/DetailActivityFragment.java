package com.example.shevchenko.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shevchenko.movies.ApiKey;
import com.example.shevchenko.movies.R;
import com.example.shevchenko.movies.adapters.ReviewAdapter;
import com.example.shevchenko.movies.adapters.TrailerAdapter;
import com.example.shevchenko.movies.model.Movie;
import com.example.shevchenko.movies.model.Review;
import com.example.shevchenko.movies.model.Video;
import com.example.shevchenko.movies.rest.ApiResponse.ReviewApiResponse;
import com.example.shevchenko.movies.rest.ApiResponse.VideoApiResponse;
import com.example.shevchenko.movies.rest.RestClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A nophoto fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.rating) TextView mRating;
    @Bind(R.id.release) TextView mReleaseDate;
    @Bind(R.id.plot) TextView mPlot;
    @Bind(R.id.poster) ImageView mPoster;
    @Bind(R.id.trailers) ListView mTrailerList;
    @Bind(R.id.reviews) ListView mReviews;
    @Bind(R.id.favorite)
    Button mLike;
    List<Video> trailers;
    Movie mMovie;
    Context mContext;
    ShareActionProvider mShareActionProvider;

    // This function was taken from Stackoverflow answers as ready solution on how to put
    // ListView inside ScrollView and make it look good
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public DetailActivityFragment() {
    }

    private void getTrailers(Movie movie) {
        Call<VideoApiResponse> call = RestClient.getService().getVideos(movie.getId(), ApiKey.getApiKey());

        call.enqueue(new Callback<VideoApiResponse>() {
            @Override
            public void onResponse(Response<VideoApiResponse> response, Retrofit retrofit) {
                VideoApiResponse videos = response.body();

                if (videos != null) {
                    trailers = videos.videos;
                    // When we get trailers list, we should update share intent to add trailer's URL
                    if (mMovie != null) setShareIntent();

                    mTrailerList.setAdapter(new TrailerAdapter(getContext(), trailers));
                    setListViewHeightBasedOnChildren(mTrailerList);

                    mTrailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent showVideo = new Intent(Intent.ACTION_VIEW, trailers.get(position).getUri());
                            startActivity(showVideo);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO
            }
        });
    }

    private void getReviews(Movie movie) {
        Call<ReviewApiResponse> call = RestClient.getService().getReviews(movie.getId(), ApiKey.getApiKey());

        call.enqueue(new Callback<ReviewApiResponse>() {
            @Override
            public void onResponse(Response<ReviewApiResponse> response, Retrofit retrofit) {
                ReviewApiResponse reviewResponse = response.body();
                if (reviewResponse != null) {
                    final List<Review> reviews = reviewResponse.reviews;
                    mReviews.setAdapter(new ReviewAdapter(getContext(), reviews));
                    setListViewHeightBasedOnChildren(mReviews);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO
            }
        });
    }

    private void setShareIntent() {
        Intent myShareIntent = new Intent();
        myShareIntent.setAction(Intent.ACTION_SEND);
        String shareText = mMovie.getTitle();
        if (trailers != null && trailers.size() > 0) {
            shareText = shareText + " " + trailers.get(0).getUri().toString();
        }
        myShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        myShareIntent.setType("text/plain");
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(myShareIntent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mMovie != null) setShareIntent();
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        mContext = getContext();

        Bundle arguments = this.getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Movie.EXTRA_NAME);
        }

        if (mMovie != null) {
            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(getResources().getString(R.string.release_date,
                    mMovie.getReleaseDate()));

            // TODO Idea - show 3-5 lines and make it expandable
            mPlot.setText(mMovie.getOverview());
            mRating.setText(getResources().getString(R.string.rating,
                    mMovie.getVoteAverage()));
            //TODO Change to icons
            //mLike.setImageResource(R.drawable.ic_favorite_black_24dp);
            mLike.setText(mMovie.isFavorite(mContext) ? "Unlike" : "Like");

            mLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMovie.isFavorite(mContext)) {
                        mMovie.deleteFavorite(mContext);
                        mLike.setText("Like");
                    } else {
                        mMovie.addFavorite(mContext);
                        mLike.setText("Unlike");
                    }
                }
            });

            getTrailers(mMovie);
            // TODO Add reviews block title if any reviews present
            getReviews(mMovie);

            Picasso.with(getActivity())
                    .load(mMovie.getPosterPath(Movie.DEFAULT_SIZE))
                    .placeholder(R.drawable.placeholder)
                    .into(mPoster);
        }
        return rootView;
    }
}


