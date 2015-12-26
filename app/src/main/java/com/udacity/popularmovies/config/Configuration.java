package com.udacity.popularmovies.config;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dinesh Karpe on 12/22/15.
 */
public class Configuration implements Parcelable{

    public String sortOrder;

    public Configuration(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Configuration(Parcel parcel){
            this.sortOrder = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator<Configuration>() {
        @Override
        public Configuration createFromParcel(Parcel in) {
            return new Configuration(in);
        }

        @Override
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
}
