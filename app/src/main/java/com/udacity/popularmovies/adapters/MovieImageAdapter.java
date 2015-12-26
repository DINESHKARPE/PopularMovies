package com.udacity.popularmovies.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.udacity.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Dinesh Karpe on 12/21/15.
 */
public class MovieImageAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<String> moviePosterurlList;
    private int movieImageWidth;

    public MovieImageAdapter(Context context){
        this.context = context;
        moviePosterurlList = new ArrayList<>();
    }

    public void add(String moviePosterUrl) {
        moviePosterurlList.add(moviePosterUrl);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return moviePosterurlList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviePosterurlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setMovieImageWidth(int movieImageWidth) {
        this.movieImageWidth = movieImageWidth;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(context);
            int height = (int)(this.movieImageWidth * 1.5);
            imageView.setLayoutParams(new GridView.LayoutParams(this.movieImageWidth, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }
        
        Picasso.with(context)
                .load(moviePosterurlList.get(position))
                .placeholder(R.drawable.loading_image)
                .into(imageView);
        return imageView;
    }

    public ArrayList<String> getMoviePosterurlList() {
        return moviePosterurlList;
    }

    public void setMoviePosterurlList(ArrayList<String> moviePosterurlList) {
        this.moviePosterurlList = moviePosterurlList;
    }
}
