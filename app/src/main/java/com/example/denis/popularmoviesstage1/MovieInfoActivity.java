package com.example.denis.popularmoviesstage1;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denis.popularmoviesstage1.database.MovieContract;
import com.example.denis.popularmoviesstage1.database.MovieDBHelper;
import com.example.denis.popularmoviesstage1.interfaces.MovieInfoActivityAsyncTaskResponse;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import utilttes.NetworkUtilites;

public class MovieInfoActivity extends AppCompatActivity implements MovieInfoActivityAsyncTaskResponse, MovieInfoActivityTrailersAdapter.OnTrailerClickListener {

    private SQLiteDatabase mDb;
    String movieId = "";
    String posterLink = "";
    String movieTitle = "";
    String movieReleaseDate = "";
    String movieUserRating = "";
    String movieSynopsis = "";
    String trailersResult;
    String reviewsResult;
    ArrayList<TrailerInfo> trailersArray;
    ArrayList<ReviewInfo> reviewsArray;
    private MovieInfoActivityTrailersAdapter trailersAdapter;
    private MovieInfoActivityReviewsAdapter reviewsAdapter;

    private static final String SAVED_TRAILERS_RESULT = "trailersResult";
    private static final String SAVED_REVIEWS_RESULT = "reviewsResult";

    //using ButterKnife library to more compact code
    @BindView(R.id.posterImageView) ImageView moviePosterView;
    @BindView(R.id.titleTextView) TextView movieTitleTextView;
    @BindView(R.id.releaseDateTextView) TextView releaseDateTextView;
    @BindView(R.id.userRatingTextView) TextView userRatingTextView;
    @BindView(R.id.plotSynopsisTextView) TextView plotSynopsisTextView;
    @BindView(R.id.addToFavoriteMovie) Button addToFavoriteButton;
    @BindView(R.id.trailersRecyclerView) RecyclerView trailersRecyclerView;
    @BindView(R.id.reviewsRecyclerView) RecyclerView reviewsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        ButterKnife.bind(this);

        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        trailersRecyclerView.setLayoutManager(trailersLayoutManager);
        trailersRecyclerView.setHasFixedSize(true);
        trailersRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setNestedScrollingEnabled(false);

//        MovieDBHelper dbHelper = new MovieDBHelper(this);
//        // Keep a reference to the mDb until paused or killed. Get a writable database
//        // because you will be adding restaurant customers
//        mDb = dbHelper.getWritableDatabase();


//        Cursor cursor = getAllGuests();
//        Log.d("path",mDb.getPath());
//        Log.d("maxSize",String.valueOf(mDb.getMaximumSize()));
//        Log.d("pageSize",String.valueOf(mDb.getPageSize()));
//        Log.d("isOpen",String.valueOf(mDb.isOpen()));
//        Log.d("path",mDb.getPath());


        Intent receivedIntent = getIntent();
        MovieInfo movieInfo = (MovieInfo) getIntent().getParcelableExtra("movieInfo");
        movieId = movieInfo.movieId;
        posterLink = movieInfo.moviePosterImageThumbNail;
        movieTitle = movieInfo.originalTitle;
        movieReleaseDate = movieInfo.releaseDate;
        movieUserRating = movieInfo.userRating;
        movieSynopsis = movieInfo.plotSynopsis;

        if (movieId!=null) {

            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(SAVED_TRAILERS_RESULT)) {
                    trailersResult = savedInstanceState
                            .getString(SAVED_TRAILERS_RESULT);
                    Log.d("b1", trailersResult);
                    renderTrailers(trailersResult);
                }
                if (savedInstanceState.containsKey(SAVED_REVIEWS_RESULT)) {
                    reviewsResult = savedInstanceState
                            .getString(SAVED_REVIEWS_RESULT);
                    Log.d("b2", reviewsResult);

                    renderReviews(reviewsResult);
                }
            }
            else if (NetworkUtilites.isOnline(MovieInfoActivity.this)) {
                Log.d("movieId",movieId);
                URL trailersApiUrl = NetworkUtilites.buildMovieTrailersUrl(movieId);
                MovieApiQueryTask trailersAsyncTask = new MovieApiQueryTask();
                trailersAsyncTask.movieInfoActivityDelegate = this;
                trailersAsyncTask.requestType = "trailers";
                trailersAsyncTask.execute(trailersApiUrl);

                URL reviewsApiUrl = NetworkUtilites.buildMovieReviewsUrl(movieId);
                Log.d("reviewUrl", reviewsApiUrl.toString());
                MovieApiQueryTask reviewsAsyncTask = new MovieApiQueryTask();
                reviewsAsyncTask.movieInfoActivityDelegate = this;
                reviewsAsyncTask.requestType = "reviews";
                reviewsAsyncTask.execute(reviewsApiUrl);

            }
        }

        if (posterLink!=null) {
            Log.d("link",posterLink);
            Picasso mPicasso = Picasso.with(MovieInfoActivity.this);

            //Checkinf if image load from memory or Network (should be load from memory for smooth user experience)
//            mPicasso.setIndicatorsEnabled(true);

            mPicasso.load("http://image.tmdb.org/t/p/w185/"+posterLink)
                    .into(moviePosterView);

        }
        if (movieTitle!=null) {
            movieTitleTextView.setText(movieTitle);
        }
        if (movieReleaseDate!=null) {
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
        if (movieUserRating!=null) {
            userRatingTextView.setText(movieUserRating+"/10");
        }
        if (movieSynopsis!=null) {
            plotSynopsisTextView.setText(movieSynopsis);
        }





        addToFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This action takes only 3-4 milliseconds in total. Don't think, that i need to use another thread to do this request.
                // In addition, user pushed the button and waits the result.
                addNewFavoriteMovie(movieId, posterLink, movieTitle, movieReleaseDate,movieUserRating,movieSynopsis);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SAVED_TRAILERS_RESULT, trailersResult);
        outState.putString(SAVED_REVIEWS_RESULT, reviewsResult);
    }

    @Override
    public void trailersRequestFinish(String result) {
        if (MovieInfoActivity.this.isFinishing()) {
            return;
        }
        if (result != null && !result.equals("")) {
            trailersResult = result;
            renderTrailers(result);
        }
    }

    @Override
    public void reviewsRequestFinish(String result) {
        if (MovieInfoActivity.this.isFinishing()) {
            return;
        }
        if (result != null && !result.equals("")) {
            reviewsResult = result;
            renderReviews(result);
        }
    }

    @Override
    public void onItemClick(TrailerInfo item) {
        Log.d("trailerid",item.key);
        if (item.site.equals("YouTube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+item.key));
            startActivity(intent);
        }
    }

    private void addNewFavoriteMovie (String movieId, String posterLink, String movieTitle, String movieReleaseDate, String movieUserRating, String movieSynopsis) {

        if (isMovieInList(Integer.parseInt(movieId))) {
            if (!(MovieInfoActivity.this).isFinishing()) {
                new DialogBuilder(this,getResources().getString(R.string.error),getResources().getString(R.string.movie_already_in_list));
            }
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieDBEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(MovieContract.MovieDBEntry.COLUMN_MOVIE_TITLE, movieTitle);
        cv.put(MovieContract.MovieDBEntry.COLUMN_POSTER_PATH, posterLink);
        cv.put(MovieContract.MovieDBEntry.COLUMN_MOVIE_OVERVIEW, movieSynopsis);
        cv.put(MovieContract.MovieDBEntry.COLUMN_VOTE_AVERAGE, movieUserRating);
        cv.put(MovieContract.MovieDBEntry.COLUMN_RELEASE_DATE, movieReleaseDate);
        Uri uri = getContentResolver().insert(MovieContract.MovieDBEntry.CONTENT_URI, cv);
        Log.d("uri", uri.toString());

        if (uri!=null) {
            new DialogBuilder(this,getResources().getString(R.string.success),getResources().getString(R.string.movie_successfully_added));
        }
        else {

        }
    }
    private boolean isMovieInList(int movieId) {
        Cursor checkCursor = getContentResolver().query(
                MovieContract.MovieDBEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(movieId)).build(),
                null,
                null,
                null,
                null,
                null
        );

        return checkCursor.getCount()>0;
    }
    private void renderTrailers(String result) {
        Log.d("trailers", result);
        trailersArray = new ArrayList<TrailerInfo>();
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray trailersJsonArray = resultObject.getJSONArray("results");
            int trailersJsonArrayLength = trailersJsonArray.length();
            for(int i=0; i < trailersJsonArray.length(); i++) {
                JSONObject movieDataObject = trailersJsonArray.getJSONObject(i);
                String id = movieDataObject.getString("id");
                String key = movieDataObject.getString("key");
                String name = movieDataObject.getString("name");
                String site = movieDataObject.getString("site");
                String size = movieDataObject.getString("size");
                String type = movieDataObject.getString("type");
                trailersArray.add(new TrailerInfo(id, key, name, site, size, type));
            }

            trailersAdapter = new MovieInfoActivityTrailersAdapter(trailersJsonArrayLength,trailersArray, MovieInfoActivity.this, MovieInfoActivity.this);
            trailersRecyclerView.setAdapter(null);
            trailersRecyclerView.setAdapter(trailersAdapter);

        }catch(JSONException e) {
        }
    }
    private void renderReviews(String result) {
        reviewsArray = new ArrayList<ReviewInfo>();
        try {
            JSONObject resultObject = new JSONObject(result);
            JSONArray reviewsJsonArray = resultObject.getJSONArray("results");
            int reviewsJsonArrayLength = reviewsJsonArray.length();
            for(int i=0; i < reviewsJsonArray.length(); i++) {
                JSONObject movieDataObject = reviewsJsonArray.getJSONObject(i);
                String author = movieDataObject.getString("author");
                String content = movieDataObject.getString("content");
                String id = movieDataObject.getString("id");
                String url = movieDataObject.getString("url");
                reviewsArray.add(new ReviewInfo(author,content,id,url));
            }

            reviewsAdapter = new MovieInfoActivityReviewsAdapter(reviewsJsonArrayLength,reviewsArray, MovieInfoActivity.this);
            reviewsRecyclerView.setAdapter(null);
            reviewsRecyclerView.setAdapter(reviewsAdapter);

        }catch(JSONException e) {
        }
    }
}
