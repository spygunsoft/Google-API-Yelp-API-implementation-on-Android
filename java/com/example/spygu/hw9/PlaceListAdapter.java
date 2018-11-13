package com.example.spygu.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PlaceListAdapter extends RecyclerView.Adapter<PlaceListAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewPlaceName;
        public TextView textViewPlaceAddress;
        public ImageView imageViewCategory;
        public ImageView imageViewFavourites;
        public ConstraintLayout main;

        public ViewHolder(View itemView) {
            super(itemView);
            main = itemView.findViewById(R.id.main);
            textViewPlaceName = (TextView) itemView.findViewById(R.id.textViewPlaceName);
            textViewPlaceAddress = (TextView) itemView.findViewById(R.id.textViewPlaceAddress);
            imageViewCategory = (ImageView) itemView.findViewById(R.id.imageViewReviewPhoto);
            imageViewFavourites = (ImageView) itemView.findViewById(R.id.imageViewFavourites);
            main.setOnClickListener(this);
            imageViewFavourites.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.main){
                Gson gson = new Gson();
                Intent placeDetailIntent = new Intent(mcontext, PlaceDetailActivity.class);
                placeDetailIntent.putExtra("from",mRequestFrom);
                placeDetailIntent.putExtra("placeListData",gson.toJson(mPlaceList));
                placeDetailIntent.putExtra("name",textViewPlaceName.getText().toString());
                placeDetailIntent.putExtra("placeId",textViewPlaceName.getContentDescription().toString());
                placeDetailIntent.putExtra("address",textViewPlaceAddress.getText().toString());
                placeDetailIntent.putExtra("bookmarked",imageViewFavourites.getContentDescription().toString());
                placeDetailIntent.putExtra("category",imageViewCategory.getContentDescription().toString());
                mcontext.startActivity(placeDetailIntent);

            }else if(v.getId() == R.id.imageViewFavourites) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                ArrayList<PlaceCustom> favourites = new ArrayList<PlaceCustom>();

                Context context = mcontext;
                int duration = Toast.LENGTH_SHORT;
                Toast toast;


                if(sharedPreferences.getString("favouriteList",null) != null){
                    Type listType = new TypeToken<ArrayList<PlaceCustom>>(){}.getType();
                    favourites = gson.fromJson(sharedPreferences.getString("favouriteList",null),listType);
                }

                if(imageViewFavourites.getContentDescription().toString() == "true"){
                    
                    int removeIndex = -1;

                    for(int i = 0; i < favourites.size();i++)
                    {
                        if(favourites.get(i).getPlaceId().equals(textViewPlaceName.getContentDescription().toString())){
                            removeIndex = i;
                        }
                        
                    }

                    favourites.remove(removeIndex);



                    imageViewFavourites.setContentDescription(Boolean.toString(false));
                    imageViewFavourites.setImageResource(R.drawable.heart_outline_black);

                    if(mRequestFrom.equals("favourites")){
                        mPlaceList.remove(getPosition());
                        notifyItemRemoved(getPosition());
                        notifyItemRangeChanged(getPosition(), mPlaceList.size());

                        if(favourites.size() == 0) {
                            TextView textView = (TextView) ((Activity) context).findViewById(R.id.textViewFavouritesListStatus);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }

                    toast = Toast.makeText(context, textViewPlaceName.getText().toString() + " was removed from favourites ", duration);
                }else{
                    favourites.add(new PlaceCustom(
                            textViewPlaceName.getContentDescription().toString(),
                            textViewPlaceName.getText().toString(),
                            imageViewCategory.getContentDescription().toString(),
                            textViewPlaceAddress.getText().toString(),
                            true
                    ));

                    notifyItemChanged(getPosition());

                    imageViewFavourites.setContentDescription(Boolean.toString(true));
                    imageViewFavourites.setImageResource(R.drawable.heart_fill_red);
                    mPlaceList.get(getPosition()).setBookmarked(true);

                    toast = Toast.makeText(context, textViewPlaceName.getText().toString() + " was added to favourites ", duration);
                }

                //editor.remove("favouriteList");

                toast.show();
                String json = gson.toJson(favourites);
                editor.putString("favouriteList",json);
                editor.apply();
            }
        }
    }

    private List<PlaceCustom> mPlaceList;
    private Context mcontext;
    private String mRequestFrom;

    public PlaceListAdapter(List<PlaceCustom> placeList, Context context, String requestFrom){
        mPlaceList = placeList;
        mcontext = context;
        mRequestFrom = requestFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View placeListView = inflater.inflate(R.layout.place_list_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(placeListView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull PlaceListAdapter.ViewHolder viewHolder, int position) {

        PlaceCustom place = mPlaceList.get(position);

        TextView textViewName = viewHolder.textViewPlaceName;
        textViewName.setText(place.getName());
        textViewName.setContentDescription(place.getPlaceId());

        TextView textViewAddress = viewHolder.textViewPlaceAddress;
        textViewAddress.setText(place.getAddress());

        ImageView imageViewFavourites = viewHolder.imageViewFavourites;
        imageViewFavourites.setImageResource(place.isBookmarked()? R.drawable.heart_fill_red:R.drawable.heart_outline_black);
        imageViewFavourites.setContentDescription(Boolean.toString(place.isBookmarked()));

        ImageView imageViewCategory = viewHolder.imageViewCategory;
        imageViewCategory.setContentDescription(place.getCategory());
        Picasso.get().load(place.getCategory()).into(imageViewCategory);

    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }


}
