package com.example.spygu.hw9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>  {

    public class ViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageViewReviewPhoto;
        public TextView textViewReviewName;
        public RatingBar ratingBarReview;
        public TextView textViewDate;
        public TextView textViewReview;
        public ConstraintLayout main;


        public ViewHolder(View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            imageViewReviewPhoto = itemView.findViewById(R.id.imageViewReviewPhoto);
            textViewReviewName = itemView.findViewById(R.id.textViewReviewName);
            ratingBarReview = itemView.findViewById(R.id.ratingBarReview);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewReview = itemView.findViewById(R.id.textViewReview);
            main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.imageViewPhoto) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setData(Uri.parse(imageViewPhoto.getContentDescription().toString()));
                mcontext.startActivity(intent);
            }

        }
    }

    private List<ReviewCustom> mReviews;
    private Context mcontext;
    private String mRequestFrom;

    public ReviewsAdapter(List<ReviewCustom> reviews, Context context, String requestFrom){
        mReviews = reviews;
        mcontext = context;
        mRequestFrom = requestFrom;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reviewsView = inflater.inflate(R.layout.reviews_layout,parent,false);

        ReviewsAdapter.ViewHolder viewHolder = new ReviewsAdapter.ViewHolder(reviewsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder viewHolder, int position) {
        ReviewCustom review = mReviews.get(position);


        ImageView imageViewReviewPhoto = viewHolder.imageViewReviewPhoto;
        imageViewReviewPhoto.setContentDescription(review.getmUrl().toString());
        Picasso.get().load(review.getmPhotoUrl()).into(imageViewReviewPhoto);

        TextView textViewReviewName = viewHolder.textViewReviewName;
        textViewReviewName.setText(review.getmName().toString());

        RatingBar ratingBarReview = viewHolder.ratingBarReview;
        ratingBarReview.setRating(Float.parseFloat(review.getmRating().toString()));

        TextView textViewDate = viewHolder.textViewDate;
        textViewDate.setText(review.getmTime().toString());

        TextView textViewReview = viewHolder.textViewReview;
        textViewReview.setText(review.getmText().toString());


    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }


}
