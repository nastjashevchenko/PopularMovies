package com.example.shevchenko.movies;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shevchenko.movies.Model.Movie;
import com.example.shevchenko.movies.Rest.ApiResponse.VideoApiResponse;
import com.example.shevchenko.movies.Rest.RestClient;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.rating) TextView mRating;
    @Bind(R.id.release) TextView mReleaseDate;
    @Bind(R.id.plot) TextView mPlot;
    @Bind(R.id.poster) ImageView mPoster;
    @Bind(R.id.trailer) Button mTrailer;

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
        mPlot.setText(mMovie.getOverview());
        mRating.setText(getResources().getString(R.string.rating,
                mMovie.getVoteAverage()));

        Picasso.with(getActivity())
                .load(mMovie.getPosterPath(Movie.DEFAULT_SIZE))
                .placeholder(R.drawable.placeholder)
                .into(mPoster);


        mTrailer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Activity host = (Activity) v.getContext();
                Movie movie = host.getIntent().getParcelableExtra(Movie.EXTRA_NAME);
                Call<VideoApiResponse> call = RestClient.getService().getVideos(
                        movie.getId(),
                        ApiKey.getApiKey()
                );
                call.enqueue(new Callback<VideoApiResponse>() {
                    @Override
                    // TODO Understand what video is main
                    // TODO add getUrl to video
                    public void onResponse(Response<VideoApiResponse> response, Retrofit retrofit) {
                        VideoApiResponse videos = response.body();
                        String key = videos.videos.get(0).getKey();
                        Uri builder = Uri.parse("http://youtube.com/watch").buildUpon()
                                .appendQueryParameter("v", key)
                                .build();

                        startActivity(new Intent(Intent.ACTION_VIEW, builder));
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        // TODO
                    }
                });
            }
        });

        return rootView;
    }
}


