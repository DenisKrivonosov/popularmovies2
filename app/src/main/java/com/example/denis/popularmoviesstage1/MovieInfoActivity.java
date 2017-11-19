package com.example.denis.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MovieInfoActivity extends AppCompatActivity {
    private ImageView moviePosterView;
    private TextView movieTitleTextView;
    private TextView releaseDateTextView;
    private TextView userRatingTextView;
    private TextView plotSynopsisTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        moviePosterView =  findViewById(R.id.posterImageView);
        movieTitleTextView =  findViewById(R.id.titleTextView);
        releaseDateTextView =  findViewById(R.id.releaseDateTextView);
        userRatingTextView =  findViewById(R.id.userRatingTextView);
        plotSynopsisTextView =  findViewById(R.id.plotSynopsisTextView);
        Intent receivedIntent = getIntent();


        if (receivedIntent.hasExtra("moviePosterLink")) {
            String posterLink = receivedIntent.getStringExtra("moviePosterLink");
            Log.d("link",posterLink);
            Picasso mPicasso = Picasso.with(MovieInfoActivity.this);

            //Checkinf if image load from memory or Network (should be load from memory for smooth user experience)
//            mPicasso.setIndicatorsEnabled(true);

            mPicasso.load("http://image.tmdb.org/t/p/w185/"+posterLink)
                    .into(moviePosterView);

        }
        if (receivedIntent.hasExtra("movieTitle")) {
            String movieTitle = receivedIntent.getStringExtra("movieTitle");
            movieTitleTextView.setText(movieTitle);
        }
        if (receivedIntent.hasExtra("movieReleaseDate")) {
            String movieReleaseDate = receivedIntent.getStringExtra("movieReleaseDate");
            String movieReleaseYear = "";

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date parsedDate = parser.parse(movieReleaseDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                movieReleaseYear = String.valueOf(calendar.get(Calendar.YEAR));
                releaseDateTextView.setText(movieReleaseYear);

            }
            catch (Exception e) {
                Log.e("error", "error in parsing date");
                releaseDateTextView.setText(movieReleaseDate);
            }

        }
        if (receivedIntent.hasExtra("movieUserRating")) {
            String movieUserRating = receivedIntent.getStringExtra("movieUserRating");
            userRatingTextView.setText(movieUserRating+"/10");
        }
        if (receivedIntent.hasExtra("movieSynopsis")) {
            String movieSynopsis = receivedIntent.getStringExtra("movieSynopsis");
            plotSynopsisTextView.setText(movieSynopsis);
        }

    }
}
