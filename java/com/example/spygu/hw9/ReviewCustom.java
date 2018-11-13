package com.example.spygu.hw9;

public class ReviewCustom {
    private String mUrl;
    private String mPhotoUrl;
    private String mName;
    private String mRating;
    private String mTime;
    private String mText;

    public ReviewCustom(String url,String photoUrl,String name,String rating,String time,String text){
        mUrl = url;
        mPhotoUrl = photoUrl;
        mName = name;
        mRating = rating;
        mTime = time;
        mText = text;

    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }
}
