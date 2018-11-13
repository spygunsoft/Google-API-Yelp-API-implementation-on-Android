package com.example.spygu.hw9;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TabFavouritesFragment extends Fragment {
    public static final String TAG = "tab_favourites_fragment";



    public TabFavouritesFragment() {
    }

    public static TabFavouritesFragment newInstance(int sectionNumber) {
        TabFavouritesFragment fragment = new TabFavouritesFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView recyclerViewFavouritesList = (RecyclerView) rootView.findViewById(R.id.recyclerViewFavouritesList);
        TextView textViewFavouritesListStatus = (TextView) rootView.findViewById(R.id.textViewFavouritesListStatus);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        ArrayList<PlaceCustom> favourites = new ArrayList<PlaceCustom>();

        if(sharedPreferences.getString("favouriteList",null) != null){
            Type listType = new TypeToken<ArrayList<PlaceCustom>>(){}.getType();
            favourites = gson.fromJson(sharedPreferences.getString("favouriteList",null),listType);
        }



        PlaceListAdapter adapter = new PlaceListAdapter(favourites,getContext(),"favourites");

        if(adapter.getItemCount() > 0){

            textViewFavouritesListStatus.setVisibility(View.INVISIBLE);
        }else{

            textViewFavouritesListStatus.setVisibility(View.VISIBLE);
        }



        recyclerViewFavouritesList.setAdapter(adapter);

        recyclerViewFavouritesList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }
}
