package com.example.spygu.hw9;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabMapFragment extends Fragment {

    public static final String TAG = "tab_info_fragment";

    public TabMapFragment() {
        // Required empty public constructor
    }



    public static TabMapFragment newInstance(int sectionNumber) {
        TabMapFragment fragment = new TabMapFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tab_map, container, false);



        return rootView;
    }


}
