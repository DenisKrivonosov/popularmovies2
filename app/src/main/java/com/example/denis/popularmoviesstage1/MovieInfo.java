package com.example.denis.popularmoviesstage1;

/**
 * Created by Denis on 11/15/2017.
 */

 class MovieInfo {
    String originalTitle;
    String moviePosterImageThumbNail;
    String plotSynopsis;
    String userRating;
    String releaseDate;

        public MovieInfo(String title, String posterImage, String synopsis, String rating, String date)
        {
            this.originalTitle = title;
            this.moviePosterImageThumbNail = posterImage;
            this.plotSynopsis = synopsis;
            this.userRating = rating;
            this.releaseDate = date;
        }

}
