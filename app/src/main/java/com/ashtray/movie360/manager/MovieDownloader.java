package com.ashtray.movie360.manager;

import android.app.Activity;
import android.util.Log;

import com.ashtray.movie360.MyApplication;
import com.ashtray.movie360.R;
import com.ashtray.movie360.entities.MovieReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDownloader {

    public interface DownloaderCallBack {
        void onDownloadSuccess(List<MovieReview> movieReviews);
        void onDownloadFailed(String errorMessage);
    }

    private DownloaderCallBack downloaderCallBack;

    public MovieDownloader(DownloaderCallBack downloaderCallBack) {
        this.downloaderCallBack = downloaderCallBack;
    }

    public void downloadMovie(Activity activity, int currentIndex) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            tryToDownloadMovie(currentIndex);
            return;
        }

        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (!task.isSuccessful()) {
                if(downloaderCallBack != null)
                    downloaderCallBack.onDownloadFailed("Not complete");
                return;
            }

            tryToDownloadMovie(currentIndex);
        });
    }

    private void tryToDownloadMovie(int currentIndex) {
        String collectionName = MyApplication.getInstance().getString(R.string.movie_review_collection);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection(collectionName);
        collectionReference.get().addOnSuccessListener(documents -> {

            String fieldMovieName = MyApplication.getInstance().getString(R.string.mr_field_movie_name);
            String fieldMoviePoster = MyApplication.getInstance().getString(R.string.mr_field_image_string);
            String fieldMovieReleaseYear = MyApplication.getInstance().getString(R.string.mr_field_release_year);

            try {
                List<MovieReview> movieReviews = new ArrayList<>();
                int counter = 0;

                for (QueryDocumentSnapshot document : documents) {

                    Log.d("[gp]", "casting document " + counter++);

                    MovieReview movieReview = new MovieReview();
                    Map<String, Object> docObject = document.getData();

                    if(docObject.containsKey(fieldMovieName)){
                        movieReview.setMovieName((String) docObject.get(fieldMovieName));
                        Log.d("[gp]", "movie name " + movieReview.getMovieName());
                    }
                    if(docObject.containsKey(fieldMovieReleaseYear)){
                        movieReview.setReleaseDate((String) docObject.get(fieldMovieReleaseYear));
                        Log.d("[gp]", "release date " + movieReview.getReleaseDate());
                    }
                    if(docObject.containsKey(fieldMoviePoster)){
                        movieReview.setMoviePoster((String) docObject.get(fieldMoviePoster));
                    }

                    movieReviews.add(movieReview);
                }

                if(downloaderCallBack != null)
                    downloaderCallBack.onDownloadSuccess(movieReviews);

            } catch (Exception e) {
                e.printStackTrace();
                if(downloaderCallBack != null)
                    downloaderCallBack.onDownloadFailed("Data processing error");
            }

        }).addOnCanceledListener(() -> {
            if(downloaderCallBack != null)
                downloaderCallBack.onDownloadFailed("Wrong ref.");
        });
    }

}
