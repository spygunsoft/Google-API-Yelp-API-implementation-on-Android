package com.example.spygu.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TabInfoFragment extends Fragment {

    public static final String TAG = "tab_info_fragment";
    protected GeoDataClient mGeoDataClient;

    public TabInfoFragment() {
        // Required empty public constructor
    }



    public static TabInfoFragment newInstance(int sectionNumber) {
        TabInfoFragment fragment = new TabInfoFragment();
        Bundle args = new Bundle();
        args.putInt(TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_tab_info, container, false);
        final TextView textViewAddress = (TextView) rootView.findViewById(R.id.infoAddress);
        final TextView textViewPhone = (TextView) rootView.findViewById(R.id.infoPhone);
        final TextView textViewPrice = (TextView) rootView.findViewById(R.id.infoPrice);
        final RatingBar ratingBarRating = (RatingBar) rootView.findViewById(R.id.infoRatingBar);
        final TextView textViewGooglePage = (TextView) rootView.findViewById(R.id.infoGoogle);
        final TextView textViewWebsite = (TextView) rootView.findViewById(R.id.infoWebsite);

        String placeId = getActivity().getIntent().getExtras().getString("placeId");

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        String url ="https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeId+"&key=AIzaSyCDpsp8T4OexiPU30EIOtpKkUedwIuy0Is";

        JsonObjectRequest jsonLocationRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject place = new JSONObject();
                        place = response;
                        try {
                            String mAddress;
                            if(place.getJSONObject("result").has("formatted_address")){
                                mAddress = place.getJSONObject("result").get("formatted_address").toString();
                            }else{
                                mAddress = "";
                            }

                            final String mPhone;
                            if(place.getJSONObject("result").has("formatted_phone_number")){
                                mPhone = place.getJSONObject("result").get("formatted_phone_number").toString();
                            }else{
                                mPhone = "";
                            }

                            int mPrice;
                            if(place.getJSONObject("result").has("price_level")){
                                mPrice = Integer.parseInt(place.optJSONObject("result").get("price_level").toString());
                            }else{
                                mPrice = 0;
                            }

                            float mRating;
                            if(place.getJSONObject("result").has("rating")){
                                mRating = (float) Double.parseDouble(place.getJSONObject("result").get("rating").toString());
                            }else{
                                mRating = 0;
                            }

                            final String mUrl;
                            if(place.getJSONObject("result").has("url")){
                                mUrl = place.getJSONObject("result").get("url").toString();
                            }else{
                                mUrl = "";
                            }

                            final String mWebsite;
                            if(place.getJSONObject("result").has("website")){
                                mWebsite = place.getJSONObject("result").get("website").toString();
                            }else{
                                mWebsite = "";
                            }

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            Gson gson = new Gson();
                            ArrayList<PhotoCustom> photos = new ArrayList<PhotoCustom>();
                            ArrayList<ReviewCustom> reviews = new ArrayList<ReviewCustom>();

                            for(int k  = 0; k<place.getJSONObject("result").getJSONArray("photos").length();k++) {
                                String photoReference = place.getJSONObject("result").getJSONArray("photos").getJSONObject(k).get("photo_reference").toString();
                                String urlFormat = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1920&maxheight=1080&photoreference=" + photoReference + "&key=AIzaSyDQkic1znJsDsLm9fPEFftxgrr-4xFRboM";

                                photos.add(new PhotoCustom(urlFormat));
                            }

                            for(int l  = 0; l<place.getJSONObject("result").getJSONArray("reviews").length();l++) {

                                String mRUrl = place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("author_url").toString();
                                String mPhotoUrl = place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("profile_photo_url").toString();
                                String mName = place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("author_name").toString();
                                String mRRating = place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("rating").toString();



                                Date df = new java.util.Date(Long.parseLong(place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("time").toString())*1000L);
                                String vv = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(df);

                                String mTime = vv;
                                String mText = place.getJSONObject("result").getJSONArray("reviews").getJSONObject(l).get("text").toString();

                                reviews.add(new ReviewCustom(mUrl,mPhotoUrl,mName,mRRating,mTime,mText));
                            }

                            String json = gson.toJson(photos);

                            for(int z=0;z<place.getJSONObject("result").getJSONArray("address_components").length();z++){
                                if(place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).getJSONArray("types").get(0).toString().equals("locality")){
                                    editor.putString("city",place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).get("short_name").toString());
                                }

                                if(place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).getJSONArray("types").get(0).toString().equals("administrative_area_level_1")){
                                    editor.putString("state",place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).get("short_name").toString());
                                }

                                if(place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).getJSONArray("types").get(0).toString().equals("country")){
                                    editor.putString("country",place.getJSONObject("result").getJSONArray("address_components").getJSONObject(z).get("short_name").toString());
                                }
                            }


                            editor.putString("address",place.getJSONObject("result").get("formatted_address").toString());
                            editor.putString("nameLocation",place.getJSONObject("result").get("name").toString());


                            editor.putString("photos",json);
                            json = gson.toJson(reviews);
                            editor.putString("reviews",json);
                            editor.apply();


                            textViewAddress.setText(mAddress);

                            SpannableString content = new SpannableString(mPhone);
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            textViewPhone.setText(content);
                            textViewPrice.setText(mPrice == 5 ? "$$$$$" : mPrice == 4 ? "$$$$" : mPrice == 3 ? "$$$" : mPrice == 2 ? "$$" : mPrice == 1 ? "$" : "");
                            ratingBarRating.setRating(mRating);

                            SpannableString content2 = new SpannableString(mUrl);
                            content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
                            textViewGooglePage.setText(content2);

                            SpannableString content3 = new SpannableString(mWebsite);
                            content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
                            textViewWebsite.setText(content3);


                            textViewPhone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+mPhone));
                                    startActivity(intent);
                                }
                            });

                            textViewGooglePage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(mUrl));
                                    startActivity(intent);
                                }
                            });

                            textViewWebsite.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(mWebsite));
                                    startActivity(intent);
                                }
                            });




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

        /*mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    PlaceBufferResponse places = task.getResult();
                    Place myPlace = places.get(0);
                    Log.i(TAG, "Place found: " + myPlace.getName());
                    places.release();
                } else {
                    Log.e(TAG, "Place not found.");
                }
            }
        });*/






        return rootView;
    }


}
