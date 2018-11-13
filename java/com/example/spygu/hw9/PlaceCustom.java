package com.example.spygu.hw9;

import java.util.ArrayList;

public class PlaceCustom {

    private String mPlaceId;
    private String mName;
    private String mCategory;
    private String mAddress;
    private boolean mBookmarked;

    public PlaceCustom(String placeId, String name, String category, String address, boolean bookmarked){
        mPlaceId = placeId;
        mName = name;
        mCategory = category;
        mAddress = address;
        mBookmarked = bookmarked;
    }

    public void setPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setBookmarked(boolean mBookmarked) {
        this.mBookmarked = mBookmarked;
    }



    public  String getPlaceId(){
        return mPlaceId;
    }

    public String getName(){
        return mName;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getAddress() {
        return mAddress;
    }

    public boolean isBookmarked() {
        return mBookmarked;
    }


}
