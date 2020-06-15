package com.ashtray.movie360.features;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ashtray.movie360.R;
import com.ashtray.movie360.entities.MovieReview;
import com.ashtray.movie360.entities.MyFragment;

import com.ashtray.movie360.manager.MovieDownloader;
import com.ashtray.movie360.manager.MovieDownloader.DownloaderCallBack;

import java.util.List;


public class FragmentHome extends MyFragment {

    private MovieDownloader movieDownloader;

    private ProgressBar progressBar;
    private ImageView imageView;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public boolean handleBackButtonPressed() {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myFragmentCallBacks.setBackButtonEnabled(false);
        myFragmentCallBacks.setActivityTitle("Score Board");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = v.findViewById(R.id.ProgressBarForMovieDownload);
        imageView = v.findViewById(R.id.ImageViewForCheck);
        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.AdminLogin) {
            myFragmentCallBacks.showFragment(MyFragmentNames.ADMIN_LOGIN);
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity activity = getActivity();
        if(activity == null) {
            myFragmentCallBacks.showToastMessage("Restart App", true);
            return;
        }

        movieDownloader = new MovieDownloader(new MovieDownloaderCallBackHandler());
        movieDownloader.downloadMovie(activity, 0);
    }

    private class MovieDownloaderCallBackHandler implements DownloaderCallBack {

        @Override
        public void onDownloadSuccess(List<MovieReview> movieReviews) {
            int total = movieReviews.size();
            myFragmentCallBacks.showToastMessage("movie downloaded " + total, true);

            if(total > 0){
                imageView.setImageBitmap(movieReviews.get(0).getMoviePoster());
            }
        }

        @Override
        public void onDownloadFailed(String errorMessage) {
            myFragmentCallBacks.showToastMessage("not downloaded", true);
        }
    }
}
