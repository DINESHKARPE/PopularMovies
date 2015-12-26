package com.udacity.popularmovies.event;

import com.udacity.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * Created by Dinesh Karpe on 12/22/15.
 */
public interface OnCompleted {

    void onTaskCompleted(ArrayList<Movie> movieArrayList);
}
