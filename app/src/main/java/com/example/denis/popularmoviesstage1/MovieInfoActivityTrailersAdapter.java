/**
 * Created by Denis on 12/16/2017.
 */

package com.example.denis.popularmoviesstage1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Denis on 11/15/2017.
 */

public class MovieInfoActivityTrailersAdapter extends RecyclerView.Adapter<MovieInfoActivityTrailersAdapter.MyViewHolder> {
    private int itemsCount;
    private final ArrayList<TrailerInfo> trailersList;
    private final OnTrailerClickListener listener;
    private final Context context;


    public interface OnTrailerClickListener {
        void onItemClick(TrailerInfo item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.trailerNameText)
        TextView trailerName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        public void bind(final TrailerInfo item, final OnTrailerClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public  MovieInfoActivityTrailersAdapter(int itemsCount, ArrayList<TrailerInfo> trailersList, OnTrailerClickListener listener, Context ctx) {
        this.trailersList = trailersList;
        this.listener = listener;
        this.context = ctx;
        this.itemsCount = itemsCount;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_info_trailer_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.trailerName.setText(context.getResources().getString(R.string.trailer)+" "+String.valueOf(position));
        holder.bind(trailersList.get(position), listener);


    }


    @Override
    public int getItemCount() {
        return itemsCount;
    }
}
