package com.example.bookingluu.Customer;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingluu.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyViewHolder> {
    Context context;
    ArrayList<Rating> ratingArrayList;

    public RatingAdapter(Context context, ArrayList<Rating> ratingArrayList) {
        this.context = context;
        this.ratingArrayList = ratingArrayList;
    }

    @NonNull
    @Override
    public RatingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.cardview_review_list,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.MyViewHolder holder, int position) {
    Rating rating = ratingArrayList.get(position);
    holder.nameText.setText("@"+rating.getCustomerName());
    holder.ratingBar.setRating(Float.parseFloat(rating.getRate()));

    holder.commentText.setText(rating.getCommentText());
    Picasso.get().load(rating.getImgURI()).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return ratingArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        RatingBar ratingBar;
        TextView nameText,commentText;
        ShapeableImageView userImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.cardRatingBar);
            nameText=itemView.findViewById(R.id.cardNameText);
            commentText=itemView.findViewById(R.id.cardCommentText);
            userImage=itemView.findViewById(R.id.cardUserImage);
        }
    }
}
