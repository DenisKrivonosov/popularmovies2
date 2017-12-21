package com.example.denis.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11/15/2017.
 */

public class MainActivityMoviesAdapter extends RecyclerView.Adapter<MainActivityMoviesAdapter.MyViewHolder> {
    private int itemsCount;
    private final ArrayList<MovieInfo> moviesList;
    private final OnItemClickListener listener;
    private final Context context;


    public interface OnItemClickListener {
        void onItemClick(MovieInfo item);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.grid_image_view) ImageView moviePoster;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void bind(final MovieInfo item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public  MainActivityMoviesAdapter(int itemsCount, ArrayList<MovieInfo> moviesList, OnItemClickListener listener, Context ctx) {
        this.moviesList = moviesList;
        this.listener = listener;
        this.context = ctx;
        this.itemsCount = itemsCount;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/"+moviesList.get(position).moviePosterImageThumbNail)
                .error(R.drawable.ic_launcher_background)
                .into(holder.moviePoster);
        holder.bind(moviesList.get(position), listener);


    }


    @Override
    public int getItemCount() {
        return itemsCount;
    }
}
