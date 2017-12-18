package com.example.denis.popularmoviesstage1;

import android.os.AsyncTask;

import com.example.denis.popularmoviesstage1.interfaces.MainActivityAsyncTaskResponse;
import com.example.denis.popularmoviesstage1.interfaces.MovieInfoActivityAsyncTaskResponse;

import java.io.IOException;
import java.net.URL;

import utilttes.NetworkUtilites;

/**
 * Created by Denis on 12/5/2017.
 */

public class MovieApiQueryTask extends AsyncTask<URL, Void, String> {
    public MainActivityAsyncTaskResponse mainActivityDelegate = null;
    public MovieInfoActivityAsyncTaskResponse movieInfoActivityDelegate = null;
    public String requestType = null;

    // Override onPreExecute to set the loading indicator to visible
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressBar.setVisibility(View.VISIBLE);
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
    protected void onPostExecute(String result) {
        switch (requestType) {
            case "movies":
                mainActivityDelegate.processFinish(result);
                break;
            case "trailers":
                movieInfoActivityDelegate.trailersRequestFinish(result);
                break;
            case "reviews":
                movieInfoActivityDelegate.reviewsRequestFinish(result);
                break;
        }
    }
}