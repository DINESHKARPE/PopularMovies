package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.movie_details);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        Movie movie = intent.getParcelableExtra("Movie_Detail");


        // get the strings and double we need from the parcelable (title, rating, description)
        ((TextView)findViewById(R.id.movieTitle)).setText(movie.getMovieTitle());
        ((TextView)findViewById(R.id.rating)).setText(getString(R.string.rated) +
                movie.getMovieRating() + getString(R.string.of_10_points));
        ((TextView)findViewById(R.id.description)).setText(movie.getMovieDescription());

        // reformat the date so it looks a little nicer and is displayed localized
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getMovieReleaseDate());
            DateFormat dateFormat =
                    android.text.format.DateFormat.getDateFormat(getApplicationContext());
            ((TextView)findViewById(R.id.releaseDate)).setText(getString(R.string.released) +
                    dateFormat.format(date));

        } catch (ParseException e) {
            //e.printStackTrace();
            // error parsing date - they must have change the format - just output string as
            // received from MovieDB
            ((TextView)findViewById(R.id.releaseDate)).setText(getString(R.string.released) +
                    getString(R.string.unknown));
        }

        Picasso.with(this)
                .load(movie.getMoviePosterUrl())
                .placeholder(R.drawable.loading_image)
                .into((ImageView)findViewById(R.id.imageView));
    }

}
