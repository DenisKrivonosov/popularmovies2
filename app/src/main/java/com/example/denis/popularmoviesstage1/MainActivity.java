package com.example.denis.popularmoviesstage1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import utilttes.NetworkUtilites;

public class MainActivity extends AppCompatActivity implements MainActivityMoviesAdapter.OnItemClickListener {

    private ProgressBar progressBar;
    private RecyclerView filmPostersRV;
    private final int GRID_SPAN_COUNT=2;
    private MainActivityMoviesAdapter mAdapter;
    private ArrayList<MovieInfo> moviesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar =  findViewById(R.id.pb_loading_indicator);
        makeMovieApiQuery("popular");
        filmPostersRV = findViewById(R.id.rv_film_posters);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
        filmPostersRV.setLayoutManager(layoutManager);
        filmPostersRV.setHasFixedSize(true);

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
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(MovieInfo movieInfo) {
        Intent movieInfoIntent = new Intent(MainActivity.this, MovieInfoActivity.class);
        movieInfoIntent.putExtra("moviePosterLink", movieInfo.moviePosterImageThumbNail);
        movieInfoIntent.putExtra("movieTitle", movieInfo.originalTitle);
        movieInfoIntent.putExtra("movieReleaseDate", movieInfo.releaseDate);
        movieInfoIntent.putExtra("movieSynopsis", movieInfo.plotSynopsis);
        movieInfoIntent.putExtra("movieUserRating", movieInfo.userRating);
        startActivity(movieInfoIntent);
    }



    private void makeMovieApiQuery(String searchType) {
        if (isOnline()) {
            URL movieApihUrl = NetworkUtilites.buildUrl(searchType);
            new MovieApiQueryTask().execute(movieApihUrl);
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

    public class MovieApiQueryTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtilites.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String requestResult) {
            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
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
                        String title = movieDataObject.getString("title");
                        String posterImage = movieDataObject.getString("poster_path");
                        String synopsis = movieDataObject.getString("overview");
                        String rating = movieDataObject.getString("vote_average");
                        String date = movieDataObject.getString("release_date");
                        moviesArray.add(new MovieInfo(title,posterImage,synopsis,rating,date));
                    }

                    mAdapter = new MainActivityMoviesAdapter(moviesJsonArrayLength,moviesArray, MainActivity.this, MainActivity.this);
                    filmPostersRV.setAdapter(null);
                    filmPostersRV.setAdapter(mAdapter);

                }catch(JSONException e) {

                }


//                mAdapter = new MainActivityMoviesAdapter(moviesArray, this);
//                filmPostersRV.setAdapter(mAdapter);
            } else {
                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
//                showErrorMessage();
            }
        }
    }
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm!=null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        else {
            return false;
        }

    }
}


