package com.udacity.popularmovies.backgroundtask;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapters.MovieImageAdapter;
import com.udacity.popularmovies.event.OnCompleted;
import com.udacity.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dinesh Karpe on 12/21/15.
 */
public class CollectMovies extends AsyncTask<String, String, ArrayList<Movie>> {

    private final String TAG = "CollectMovies";
    private int imageWidth;
    private MovieImageAdapter movieImageAdapter;
    private Context context;
    private OnCompleted listener;
    private String sortOrder;

    public CollectMovies(final String sortOrder,final int imageWidth,final MovieImageAdapter movieImageAdapter, Context context, OnCompleted listener){

        this.imageWidth = imageWidth;
        this.movieImageAdapter = movieImageAdapter;
        this.context = context;
        this.listener=listener;
        this.sortOrder = sortOrder;
    }


    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (movies != null){
            this.movieImageAdapter.setMovieImageWidth(imageWidth);
            for (Movie movie : movies){
                this.movieImageAdapter.add(movie.getMoviePosterUrl());
                Log.v(TAG, "MoviePoster: " + movie.getMoviePosterUrl());

            }

            this.movieImageAdapter.notifyDataSetChanged();
            this.listener.onTaskCompleted(movies);
        } else {
            Toast.makeText(context,
                    "Could not connect to Movie Database. Please check your Internet connection.",
                    Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        ArrayList<Movie> movieArrayList = new ArrayList<>();
        try{

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("sort_by", this.sortOrder)
                    .appendQueryParameter("vote_count.gte", "100")
                    .appendQueryParameter("api_key",
                            this.context.getResources().getString(R.string.movie_db_api_key));


            URL url = new URL(builder.build().toString());

            Log.v(TAG, "Collect Movie Built URI " + builder.build().toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            movieArrayList = getMoviesFromJson(buffer.toString());

        }catch (Exception e){
            return null;
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }


            }
        }
        return movieArrayList;
    }

    private ArrayList<Movie> getMoviesFromJson(String movieLoadedJson) throws JSONException {

        final String MDB_LIST = "results";
        final String movieTitle = "original_title";
        final String MDB_DESCRIPTION = "overview";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_RATING = "vote_average";
        final String MDB_IMAGE_SIZE = "w185";

        JSONObject moviesJson = new JSONObject(movieLoadedJson);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_LIST);


        ArrayList<Movie> moviesList = new ArrayList<>();

        for(int i = 0; i < moviesArray.length(); i++) {

            JSONObject singleMovie = moviesArray.getJSONObject(i);


            Uri.Builder movieImgBuilder = new Uri.Builder();
            movieImgBuilder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath(MDB_IMAGE_SIZE)
                    .appendEncodedPath(singleMovie.getString(MDB_POSTER_PATH));

            moviesList.add(new Movie(
                    singleMovie.getString(movieTitle),
                    movieImgBuilder.build().toString(),
                    singleMovie.getString(MDB_DESCRIPTION),
                    singleMovie.getString(MDB_RELEASE_DATE),
                    singleMovie.getDouble(MDB_RATING)));
        }

        return moviesList;

    }
}
