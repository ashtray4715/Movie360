package com.ashtray.movie360.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class MovieReview {

    private String movieName;
    private double ImdbRating;
    private String releaseDate;
    private Bitmap moviePoster;

    public String getMovieName() {
        return movieName;
    }

    public double getImdbRating() {
        return ImdbRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setImdbRating(double imdbRating) {
        ImdbRating = imdbRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Bitmap getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        byte[] decodedString = Base64.decode(moviePoster, Base64.DEFAULT);
        this.moviePoster = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
