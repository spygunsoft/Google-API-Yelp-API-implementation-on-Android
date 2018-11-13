package com.example.spygu.hw9;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class TabSearchFragment extends Fragment {
    public static final String TAG = "tab_search_fragment";

    public TabSearchFragment() {
    }

    public static TabSearchFragment newInstance(int sectionNumber) {
        TabSearchFragment fragment = new TabSearchFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView keywordError = (TextView) rootView.findViewById(R.id.keywordError);
        final TextView otherLocationError = (TextView) rootView.findViewById(R.id.otherLocationError);
        final EditText editTextKeyword = (EditText) rootView.findViewById(R.id.editTextKeyword);
        final Spinner spinnerCategory = (Spinner) rootView.findViewById(R.id.spinnerCategory);
        final EditText editTextDistance = (EditText) rootView.findViewById(R.id.editTextDistance);
        final RadioButton radioButtonOtherLocation = (RadioButton) rootView.findViewById(R.id.radioButtonOtherLocation);
        final RadioButton radioButtonCurrentLocation = (RadioButton) rootView.findViewById(R.id.radioButtonCurrentLocation);
        final EditText editTextOtherLocation = (EditText) rootView.findViewById(R.id.editTextOtherLocation);

        String [] categoryList = {"Default", "Airport", "Amusement Park", "Aquarium", "Art Gallery", "Bakery", "Bar", "Beauty Salon", "Bowling Alley", "Bus Station", "Cafe", "Campground", "Car Rental", "Casino", "Lodging", "Movie Theater", "Museum", "Night Club", "Park", "Parking", "Restaurant", "Shopping Mall", "Stadium", "Subway Station", "Taxi Stand", "Train Station", "Transit Station", "Travel Agency", "Zoo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCategory.setAdapter(adapter);

        Button buttonSearch = (Button) rootView.findViewById(R.id.buttonSearch);

        Button buttonClear = (Button) rootView.findViewById(R.id.buttonClear);

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextKeyword.setText("");
                editTextDistance.setText("");
                editTextOtherLocation.setText("");
                spinnerCategory.setSelection(0,true);
                keywordError.setVisibility(View.INVISIBLE);
                otherLocationError.setVisibility(View.INVISIBLE);
                radioButtonCurrentLocation.setChecked(true);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String keyword = (editTextKeyword.getText().toString()).replaceFirst("^\\s+", "");
                final String otherLocation = (editTextOtherLocation.getText().toString()).replaceFirst("^\\s+", "");
                final String distance = Objects.equals(editTextDistance.getText().toString(), "") ? "10" : editTextDistance.getText().toString();
                final String category = (spinnerCategory.getSelectedItem().toString()).replaceAll(" ", "_").toLowerCase();

                keywordError.setVisibility(View.INVISIBLE);
                otherLocationError.setVisibility(View.INVISIBLE);

                Context context = getActivity();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "Please fix all fields with errors", duration);


                int error = 0;

                if(keyword.length() == 0){
                    keywordError.setVisibility(View.VISIBLE);
                    error++;
                }else{
                    keywordError.setVisibility(View.INVISIBLE);
                }

                if(radioButtonOtherLocation.isChecked()){
                    if(otherLocation.length() == 0){
                        otherLocationError.setVisibility(View.VISIBLE);
                        error++;
                    }else{
                        otherLocationError.setVisibility(View.INVISIBLE);
                    }
                }

                if(error == 0){


                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    String url ="http://ip-api.com/json";

                    JsonObjectRequest jsonLocationRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    JSONObject location = new JSONObject();
                                    location = response;
                                    try {

                                        Intent placeListIntent = new Intent(getActivity(), PlaceListActivity.class);
                                        placeListIntent.putExtra("latitude", (location.get("lat")).toString());
                                        placeListIntent.putExtra("longitude", (location.get("lon")).toString());
                                        placeListIntent.putExtra("keyword", keyword);
                                        placeListIntent.putExtra("otherLocation", otherLocation);
                                        placeListIntent.putExtra("distance", distance);
                                        placeListIntent.putExtra("category", category);

                                        startActivity(placeListIntent);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });

                    // Add the request to the RequestQueue.
                    queue.add(jsonLocationRequest);



                }else{
                    toast.show();
                }


            }
        });


        return rootView;
    }
}
