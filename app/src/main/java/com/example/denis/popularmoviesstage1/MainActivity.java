package com.example.denis.popularmoviesstage1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.denis.popularmoviesstage1.database.MovieContract;
import com.example.denis.popularmoviesstage1.database.MovieDBHelper;
import com.example.denis.popularmoviesstage1.interfaces.MainActivityAsyncTaskResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import utilttes.NetworkUtilites;

public class MainActivity extends AppCompatActivity implements MainActivityMoviesAdapter.OnItemClickListener,MainActivityAsyncTaskResponse {

    @BindView(R.id.pb_loading_indicator) ProgressBar progressBar;
    @BindView(R.id.rv_film_posters) RecyclerView filmPostersRV;
    private final int GRID_SPAN_COUNT=2;
    private MainActivityMoviesAdapter mAdapter;
    private ArrayList<MovieInfo> moviesArray;

    private static final String SAVED_MOVIES_ARRAY = "moviesArray";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        filmPostersRV.setLayoutManager(layoutManager);
        filmPostersRV.setHasFixedSize(true);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_MOVIES_ARRAY)) {
                moviesArray = savedInstanceState
                        .getParcelableArrayList(SAVED_MOVIES_ARRAY);
                mAdapter = new MainActivityMoviesAdapter(moviesArray.size(),moviesArray, MainActivity.this, MainActivity.this);
                filmPostersRV.setAdapter(null);
                filmPostersRV.setAdapter(mAdapter);
            }
        }
        else {
            makeMovieApiQuery("popular");
        }

//
//        MovieDBHelper dbHelper = new MovieDBHelper(this);
//        mDb = dbHelper.getReadableDatabase();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_top_sort:
                    makeMovieApiQuery("top_rated");
                return true;
            case R.id.action_popular_sort:
                    makeMovieApiQuery("popular");
                return true;
            case R.id.favotite_sort:
                getFavoriteList();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(MovieInfo movieInfo) {
        Intent movieInfoIntent = new Intent(MainActivity.this, MovieInfoActivity.class);
//        movieInfoIntent.putExtra("moviePosterLink", movieInfo.moviePosterImageThumbNail);
//        movieInfoIntent.putExtra("movieTitle", movieInfo.originalTitle);
//        movieInfoIntent.putExtra("movieReleaseDate", movieInfo.releaseDate);
//        movieInfoIntent.putExtra("movieSynopsis", movieInfo.plotSynopsis);
//        movieInfoIntent.putExtra("movieUserRating", movieInfo.userRating);
//        movieInfoIntent.putExtra("movieId", movieInfo.movieId);
        Log.d("movieIdqq",movieInfo.movieId);
        movieInfoIntent.putExtra("movieInfo", movieInfo);
        startActivity(movieInfoIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(SAVED_MOVIES_ARRAY, moviesArray);
    }

    @Override
    public void processFinish(String requestResult){
        if (MainActivity.this.isFinishing()) {
            return;
        }
        progressBar.setVisibility(View.INVISIBLE);
        if (requestResult != null && !requestResult.equals("")) {
            moviesArray = new ArrayList<MovieInfo>();
            Log.d("result",requestResult);

            try {
                JSONObject resultObject = new JSONObject(requestResult);
                JSONArray moviesJsonArray = resultObject.getJSONArray("results");
                int moviesJsonArrayLength = moviesJsonArray.length();
                for(int i=0; i < moviesJsonArrayLength; i++) {
                    JSONObject movieDataObject = moviesJsonArray.getJSONObject(i);
                    String id = movieDataObject.getString("id");
                    String title = movieDataObject.getString("title");
                    String posterImage = movieDataObject.getString("poster_path");
                    Log.d("asdasdaqqqq", posterImage);
                    String synopsis = movieDataObject.getString("overview");
                    String rating = movieDataObject.getString("vote_average");
                    String date = movieDataObject.getString("release_date");
                    Log.d("movie_id",id);
                    moviesArray.add(new MovieInfo(title, posterImage, synopsis, rating, date, id));
                }

                mAdapter = new MainActivityMoviesAdapter(moviesJsonArrayLength,moviesArray, MainActivity.this, MainActivity.this);
                filmPostersRV.setAdapter(null);
                filmPostersRV.setAdapter(mAdapter);

            }catch(JSONException e) {
            }
        } else {

        }
    }

    private void makeMovieApiQuery(String searchType) {
        if (NetworkUtilites.isOnline(MainActivity.this)) {
            URL movieApiUrl = NetworkUtilites.buildMoviesListUrl(searchType);
            progressBar.setVisibility(View.INVISIBLE);
            Log.i("here", String.valueOf(movieApiUrl));

            MovieApiQueryTask asyncTask = new MovieApiQueryTask();
            //this to set delegate/listener back to this class
            asyncTask.mainActivityDelegate = this;
            asyncTask.requestType = "movies";
            asyncTask.execute(movieApiUrl);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getResources().getString(R.string.error))
                    .setMessage(getResources().getString(R.string.no_internet))
                    .setCancelable(true)
                    .setPositiveButton(getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void getFavoriteList() {
        Cursor cursor = getFavoriteListCursor();
        int length = cursor.getCount();
        moviesArray = convertCursorToArrayList(cursor);
        mAdapter = new MainActivityMoviesAdapter(length,moviesArray, MainActivity.this, MainActivity.this);
        filmPostersRV.setAdapter(null);
        filmPostersRV.setAdapter(mAdapter);

    }
    private Cursor getFavoriteListCursor(){
        long time= System.currentTimeMillis();
        Log.d("before",String.valueOf(time));
        Cursor checkCursor = getContentResolver().query(
                MovieContract.MovieDBEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null
        );
        long time2= System.currentTimeMillis();
        Log.d("after",String.valueOf(time2));
        return checkCursor;
    }
    private ArrayList<MovieInfo> convertCursorToArrayList(Cursor cursor) {
        ArrayList<MovieInfo> moviesArray = new ArrayList<MovieInfo>();
        try {
            while (cursor.moveToNext()) {
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_MOVIE_ID)));
                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_MOVIE_TITLE));
                String posterImage = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_POSTER_PATH));
                String synopsis = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_MOVIE_OVERVIEW));
                String rating = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_VOTE_AVERAGE));
                String date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieDBEntry.COLUMN_RELEASE_DATE));
                Log.d("movie_id",posterImage);
                moviesArray.add(new MovieInfo(title, posterImage, synopsis, rating, date, id));
            }
        } finally {
            cursor.close();
        }
       return moviesArray;
    }
}


