package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import com.udacity.popularmovies.backgroundtask.CollectMovies;
import com.udacity.popularmovies.config.Configuration;
import com.udacity.popularmovies.adapters.MovieImageAdapter;
import com.udacity.popularmovies.event.OnCompleted;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.constants.Constants;

public class MainActivity extends AppCompatActivity implements OnCompleted{

    private MovieImageAdapter movieImageAdapter;
    private ArrayList<Movie> movieList;
    private GridView gridView;
    private int movieImageWidth;
    private Configuration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.movieImageAdapter = new MovieImageAdapter(this);
        this.gridView = (GridView) findViewById(R.id.movie_grid);
        this.gridView.setAdapter(this.movieImageAdapter);


        float displayMetricsDensity = getResources().getDisplayMetrics().density * 150;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        int columns = (int) ((float) screenWidth / displayMetricsDensity);

        if (columns < 2) {
            columns = 2;
        }

        this.gridView.setNumColumns(columns);
        this.movieImageWidth = screenWidth / columns;


        if ((savedInstanceState == null) || !(savedInstanceState.containsKey(Constants.State_Movies))
                || !(savedInstanceState.containsKey(Constants.PopularMovieConfig))) {

            configuration = new Configuration(Constants.PopularityDesc);
            movieList = new ArrayList<>();
            CollectMovies fetchMovies = new CollectMovies(configuration.sortOrder, this.movieImageWidth, movieImageAdapter, MainActivity.this, this);
            fetchMovies.execute();

        } else {

            configuration = savedInstanceState.getParcelable(Constants.PopularMovieConfig);

            Log.v("MainActivity", "Saved instance with name " + Constants.State_Movies + " found.");
            movieList = savedInstanceState.getParcelableArrayList(Constants.State_Movies);
            this.gridView.setAdapter(movieImageAdapter);
            movieImageAdapter.setMovieImageWidth(this.movieImageWidth);
            for (Movie movie : movieList){
                movieImageAdapter.add(movie.getMoviePosterUrl());
            }
        }

        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              Intent movieDetail = new Intent(MainActivity.this,MovieDetail.class);
              movieDetail.putExtra("Movie_Detail", movieList.get(position));
                startActivity(movieDetail);
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putParcelableArrayList(Constants.State_Movies, movieList);
        outState.putParcelable(Constants.PopularMovieConfig, configuration);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {

            if (configuration.sortOrder.equals(Constants.PopularityDesc)) {
                configuration.sortOrder = Constants.RatingDesc;
                item.setTitle(R.string.sort_by_popularity);
            } else {
                configuration.sortOrder = Constants.PopularityDesc;
                item.setTitle(R.string.sort_by_rating);
            }
            this.movieImageAdapter.getMoviePosterurlList().clear();
            CollectMovies fetchMovies = new CollectMovies(configuration.sortOrder, this.movieImageWidth, this.movieImageAdapter, MainActivity.this, this);
            fetchMovies.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCompleted(ArrayList<Movie> movieArrayList) {
        this.movieList = movieArrayList;
    }
}
