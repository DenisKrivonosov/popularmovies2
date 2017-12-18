package com.example.denis.popularmoviesstage1.interfaces;

/**
 * Created by Denis on 12/16/2017.
 */


/**
 * Created by Denis on 12/5/2017.
 */

//Interface to implement AsyncRequest in separate file
public interface MovieInfoActivityAsyncTaskResponse {
    void trailersRequestFinish(String result);
    void reviewsRequestFinish(String result);
}
