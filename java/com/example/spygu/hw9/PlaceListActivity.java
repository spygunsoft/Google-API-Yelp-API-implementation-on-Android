package com.example.spygu.hw9;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

            }
        });


        final TextView textViewStatus;
        final String[] latitude = new String[1];
        final String[] longitude = new String[1];
        final Gson gson = new Gson();



        textViewStatus = (TextView) findViewById(R.id.textViewPlaceListStatus);
        final RecyclerView recyclerViewPlaceList = (RecyclerView) findViewById(R.id.recyclerViewPlaceList);
        RequestQueue queue = Volley.newRequestQueue(this);


        String parameter = new String();
        parameter = "&lat=" + (getIntent().getExtras().getString("latitude"));
        parameter += "&lon=" + (getIntent().getExtras().getString("longitude"));
        parameter += "&keyword=" + (getIntent().getExtras().getString("keyword"));
        parameter += "&location=" + (getIntent().getExtras().getString("otherLocation"));
        parameter += "&category=" + (getIntent().getExtras().getString("category"));
        parameter += "&distance=" + (getIntent().getExtras().getString("distance"));

        final String url ="http://csci571-anugroho-php.us-east-2.elasticbeanstalk.com/getplacelist.php?"+ parameter;

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONArray arrayPlaces = response.getJSONArray("results");
                            ArrayList<PlaceCustom> places = new ArrayList<PlaceCustom>();

                            if(arrayPlaces.length() >0){ textViewStatus.setVisibility(View.INVISIBLE);}

                            for( int i = 0; i < arrayPlaces.length() ; i++){
                                //textViewStatus.setText("Response: " + arrayPlaces.getJSONObject(i).getString("vicinity") + arrayPlaces.length());
                                String placeId = arrayPlaces.getJSONObject(i).getString("place_id");
                                String name = arrayPlaces.getJSONObject(i).getString("name");
                                String category = arrayPlaces.getJSONObject(i).getString("icon");
                                String address = arrayPlaces.getJSONObject(i).getString("vicinity");

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                ArrayList<PlaceCustom> favourites = new ArrayList<PlaceCustom>();

                                if(sharedPreferences.getString("favouriteList",null) != null){
                                    Type listType = new TypeToken<ArrayList<PlaceCustom>>(){}.getType();
                                    favourites = gson.fromJson(sharedPreferences.getString("favouriteList",null),listType);
                                }

                                boolean bookmarked = false;

                                for(int j = 0; j < favourites.size();j++)
                                {
                                    if(favourites.get(j).getPlaceId().equals(placeId)){
                                        bookmarked = true;
                                    }

                                }


                                places.add(new PlaceCustom(placeId,name,category,address,bookmarked));

                            }

                            //textViewStatus.setText("Response: " + places.get(0).getPlaceId());

                            PlaceListAdapter adapter = new PlaceListAdapter(places,PlaceListActivity.this,"placelist");

                            recyclerViewPlaceList.setAdapter(adapter);

                            recyclerViewPlaceList.setLayoutManager(new LinearLayoutManager(getParent()));





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
        queue.add(jsonObjectRequest);
    }


}
