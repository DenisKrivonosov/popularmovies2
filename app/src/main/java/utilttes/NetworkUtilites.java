package utilttes;

/**
 * Created by Denis on 11/15/2017.
 */

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtilites {

    private final static String POPULAR_MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/popular";
    private final static String TOP_RATED_MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/top_rated";

    private final static String PARAM_QUERY = "api_key";

    //insert here themoviedb api key
    private final static String API_KEY = "insert_here_api_key";


    public static URL buildUrl(String type) {
        String requestUrl = null;
        if (type.equals("popular")) {
            requestUrl = POPULAR_MOVIE_BASE_URL;
        }
        else {
            requestUrl = TOP_RATED_MOVIE_BASE_URL;

        }
        Uri builtUri = Uri.parse(requestUrl).buildUpon()
                .appendQueryParameter(PARAM_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
