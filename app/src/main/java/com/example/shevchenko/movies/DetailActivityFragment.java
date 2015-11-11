package com.example.shevchenko.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shevchenko.movies.Adapters.TrailerAdapter;
import com.example.shevchenko.movies.Model.Movie;
import com.example.shevchenko.movies.Model.Video;
import com.example.shevchenko.movies.Rest.ApiResponse.VideoApiResponse;
import com.example.shevchenko.movies.Rest.RestClient;
import com.squareup.picasso.Picasso;

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
public class DetailActivityFragment extends Fragment {
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.rating) TextView mRating;
    @Bind(R.id.release) TextView mReleaseDate;
    @Bind(R.id.plot) TextView mPlot;
    @Bind(R.id.poster) ImageView mPoster;
    @Bind(R.id.trailers) ListView mTrailerList;

    // This function was taken from Stackoverflow answers as ready solution on how to put
    // ListView inside ScrollView and make it look good
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

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

        Call<VideoApiResponse> call = RestClient.getService().getVideos(mMovie.getId(),ApiKey.getApiKey());

        call.enqueue(new Callback<VideoApiResponse>() {
            @Override
            public void onResponse(Response<VideoApiResponse> response, Retrofit retrofit) {
                VideoApiResponse videos = response.body();
                final List<Video> trailers = videos.videos;
                if (trailers != null) {
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

        Picasso.with(getActivity())
                .load(mMovie.getPosterPath(Movie.DEFAULT_SIZE))
                .placeholder(R.drawable.placeholder)
                .into(mPoster);

        return rootView;
    }
}


