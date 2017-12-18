package com.example.denis.popularmoviesstage1;

/**
 * Created by Denis on 12/17/2017.
 */

/**
 * Created by Denis on 12/16/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieInfoActivityReviewsAdapter extends RecyclerView.Adapter<MovieInfoActivityReviewsAdapter.MyViewHolder> {
    private int itemsCount;
    private final ArrayList<ReviewInfo> reviewsList;
//    private final OnTrailerClickListener listener;
    private final Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviewAuthor) TextView reviewAuthor;
        @BindView(R.id.reviewContent) TextView reviewContent;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

//        public void bind(final TrailerInfo item, final OnTrailerClickListener listener) {
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override public void onClick(View v) {
//                    listener.onItemClick(item);
//                }
//            });
//        }
    }


    public  MovieInfoActivityReviewsAdapter(int itemsCount, ArrayList<ReviewInfo> trailersList, Context ctx) {
        this.reviewsList = trailersList;
        this.context = ctx;
        this.itemsCount = itemsCount;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_info_review_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.reviewAuthor.setText(context.getResources().getString(R.string.review_by)+" "+reviewsList.get(position).author);
        holder.reviewContent.setText(reviewsList.get(position).content);
//        holder.bind(trailersList.get(position), listener);


    }


    @Override
    public int getItemCount() {
        return itemsCount;
    }
}