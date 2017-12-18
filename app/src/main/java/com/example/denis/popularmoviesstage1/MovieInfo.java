package com.example.denis.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Denis on 11/15/2017.
 */

 class MovieInfo implements Parcelable {
    String originalTitle;
    String moviePosterImageThumbNail;
    String plotSynopsis;
    String userRating;
    String releaseDate;
    String movieId;

        public MovieInfo(String title, String posterImage, String synopsis, String rating, String date, String id)
        {
            this.originalTitle = title;
            this.moviePosterImageThumbNail = posterImage;
            this.plotSynopsis = synopsis;
            this.userRating = rating;
            this.releaseDate = date;
            this.movieId = id;
        }

    public MovieInfo(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.originalTitle = data[0];
        this.moviePosterImageThumbNail = data[1];
        this.plotSynopsis = data[2];
        this.userRating = data[3];
        this.releaseDate = data[4];
        this.movieId = data[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { originalTitle, moviePosterImageThumbNail, plotSynopsis, userRating,releaseDate,movieId  });
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR = new Parcelable.Creator<MovieInfo>() {

        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };

}
