package com.example.spygu.hw9;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TabPhotosFragment extends Fragment {

    public static final String TAG = "tab_photo_fragment";

    public TabPhotosFragment() {
        // Required empty public constructor
    }

    public static TabPhotosFragment newInstance(int sectionNumber) {
        TabPhotosFragment fragment = new TabPhotosFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView placeImage;
    private GeoDataClient geoDataClient;

    private List<PlacePhotoMetadata> photosDataList;
    private int currentPhotoIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tab_photos, container, false);

        RecyclerView recyclerViewPhotos = (RecyclerView) rootView.findViewById(R.id.recyclerViewPhotos);
        TextView textViewPhotosStatus = (TextView) rootView.findViewById(R.id.textViewPhotosStatus);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ArrayList<PhotoCustom> photos = new ArrayList<PhotoCustom>();

        if(sharedPreferences.getString("photos",null) != null){
            Type listType = new TypeToken<ArrayList<PhotoCustom>>(){}.getType();
            photos = gson.fromJson(sharedPreferences.getString("photos",null),listType);
        }

        //textViewPhotosStatus.setText(sharedPreferences.getString("photos",null).toString());



        PhotosAdapter adapter = new PhotosAdapter(photos,getActivity(),"favourites");

        if(adapter.getItemCount() > 0){

            textViewPhotosStatus.setVisibility(View.INVISIBLE);
        }else{

            textViewPhotosStatus.setVisibility(View.VISIBLE);
        }



        recyclerViewPhotos.setAdapter(adapter);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }




}
