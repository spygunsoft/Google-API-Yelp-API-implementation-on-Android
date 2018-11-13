package com.example.spygu.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageViewPhoto;
        public ConstraintLayout main;

        public ViewHolder(View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
            imageViewPhoto.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.imageViewPhoto) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(imageViewPhoto.getContentDescription().toString()));
                mcontext.startActivity(intent);
            }
        }
    }

    private List<PhotoCustom> mPhotos;
    private Context mcontext;
    private String mRequestFrom;

    public PhotosAdapter(List<PhotoCustom> photos, Context context, String requestFrom){
        mPhotos = photos;
        mcontext = context;
        mRequestFrom = requestFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photosView = inflater.inflate(R.layout.photos_layout,parent,false);

        PhotosAdapter.ViewHolder viewHolder = new PhotosAdapter.ViewHolder(photosView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosAdapter.ViewHolder viewHolder, int position) {
        PhotoCustom photo = mPhotos.get(position);

        ImageView imageViewCategory = viewHolder.imageViewPhoto;
        imageViewCategory.setContentDescription(photo.getmPhotoUrl());
        Picasso.get().load(photo.getmPhotoUrl()).into(imageViewCategory);

    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }


}
