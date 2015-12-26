package com.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dinesh Karpe on 12/21/15.
 */
public class Movie implements Parcelable {

    private String movieTitle;
    private String moviePosterUrl;
    private String movieDescription;
    private String movieReleaseDate;
    private double movieRating;


    public Movie(String movieTitle, String moviePosterUrl, String movieDescription, String movieReleaseDate, double movieRating) {
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.movieDescription = movieDescription;
        this.movieReleaseDate = movieReleaseDate;
        this.movieRating = movieRating;
    }

    public Movie(Parcel parcel) {

        this.movieTitle = parcel.readString();
        this.moviePosterUrl = parcel.readString();
        this.movieDescription = parcel.readString();
        this.movieReleaseDate = parcel.readString();
        this.movieRating = parcel.readDouble();
    }


    public String getMoviePosterUrl() {
        return this.moviePosterUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(moviePosterUrl);
        dest.writeString(movieDescription);
        dest.writeString(movieReleaseDate);
        dest.writeDouble(movieRating);
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }
}



