package com.example.denis.popularmoviesstage1.database;

/**
 * Created by Denis on 12/15/2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "favoritsMovie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold favoriteMovies data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + MovieContract.MovieDBEntry.TABLE_NAME + " (" +
                MovieContract.MovieDBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieDBEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieDBEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieDBEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
