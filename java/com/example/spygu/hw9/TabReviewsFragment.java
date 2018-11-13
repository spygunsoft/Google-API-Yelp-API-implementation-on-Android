package com.example.spygu.hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class TabReviewsFragment extends Fragment {

    public static final String TAG = "tab_review_fragment";

    public TabReviewsFragment() {
        // Required empty public constructor
    }



    public static TabReviewsFragment newInstance(int sectionNumber) {
        TabReviewsFragment fragment = new TabReviewsFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tab_reviews, container, false);

        final RecyclerView recyclerViewReviewsGoogle = (RecyclerView) rootView.findViewById(R.id.recyclerViewReviewsGoogle);
        final RecyclerView recyclerViewReviewsYelp = (RecyclerView) rootView.findViewById(R.id.recyclerViewReviewsYelp);
        TextView textViewReviewStatus = (TextView) rootView.findViewById(R.id.textViewReviewStatus);

        final Spinner spinnerReview = (Spinner) rootView.findViewById(R.id.spinnerReview);
        Spinner spinnerSort = (Spinner) rootView.findViewById(R.id.spinnerSort);

        String [] reviewList = {"Google reviews", "Yelp reviews"};
        String [] sortList = {"Default order", "Highest rating", "Lowest rating", "Most Recent", "Least Recent"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, reviewList);
        adapter3.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerReview.setAdapter(adapter3);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, sortList);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerSort.setAdapter(adapter2);


        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());



        spinnerReview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str =(String)parent.getSelectedItem();
                if(str.equals("Yelp reviews")){
                    recyclerViewReviewsGoogle.setVisibility(View.INVISIBLE);
                    recyclerViewReviewsYelp.setVisibility(View.VISIBLE);
                }else{
                    recyclerViewReviewsYelp.setVisibility(View.INVISIBLE);
                    recyclerViewReviewsGoogle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<ReviewCustom> reviewsGoogle = new ArrayList<ReviewCustom>();
        final ArrayList<ReviewCustom> reviewsYelp = new ArrayList<ReviewCustom>();

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String yelpName = sharedPreferences.getString("nameLocation",null);
        String yelpCity = sharedPreferences.getString("city",null);
        String yelpState = sharedPreferences.getString("state",null);
        String yelpCountry = sharedPreferences.getString("country",null);
        String yelpAddress = sharedPreferences.getString("address",null);

        String url ="http://csci571-anugroho-php.us-east-2.elasticbeanstalk.com/getyelpreview.php?name="+yelpName+"&city="+yelpCity+"&state="+yelpState+"&country="+yelpCountry+"&address="+yelpAddress;

        JsonObjectRequest jsonLocationRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject location = new JSONObject();
                        location = response;
                        try {

                            for(int x=0;x<location.getJSONArray("reviews").length();x++)
                            {
                                String yUrl = location.getJSONArray("reviews").getJSONObject(x).get("url").toString();
                                String yIUrl = location.getJSONArray("reviews").getJSONObject(x).getJSONObject("user").getString("image_url").toString();
                                String yName = location.getJSONArray("reviews").getJSONObject(x).getJSONObject("user").getString("name").toString();
                                String yRating = location.getJSONArray("reviews").getJSONObject(x).get("rating").toString();
                                String yTC = location.getJSONArray("reviews").getJSONObject(x).get("time_created").toString();
                                String yT = location.getJSONArray("reviews").getJSONObject(x).get("text").toString();
                                reviewsYelp.add( new ReviewCustom(
                                        yUrl,
                                        yIUrl,
                                        yName,
                                        yRating,
                                        yTC,
                                        yT
                                ));
                            }

                            ReviewsAdapter adapter = new ReviewsAdapter(reviewsYelp,getActivity(),"favourites");
                            recyclerViewReviewsYelp.setAdapter(adapter);

                            recyclerViewReviewsYelp.setLayoutManager(new LinearLayoutManager(getActivity()));

                            recyclerViewReviewsYelp.setVisibility(View.INVISIBLE);





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


        if(sharedPreferences.getString("reviews",null) != null){
            Type listType = new TypeToken<ArrayList<ReviewCustom>>(){}.getType();
            reviewsGoogle = gson.fromJson(sharedPreferences.getString("reviews",null),listType);
        }

        ReviewsAdapter adapter = new ReviewsAdapter(reviewsGoogle,getActivity(),"favourites");

        if(adapter.getItemCount() > 0){

            textViewReviewStatus.setVisibility(View.INVISIBLE);
        }else{

            textViewReviewStatus.setVisibility(View.VISIBLE);
        }



        recyclerViewReviewsGoogle.setAdapter(adapter);

        recyclerViewReviewsGoogle.setLayoutManager(new LinearLayoutManager(getActivity()));



        return rootView;
    }


}
