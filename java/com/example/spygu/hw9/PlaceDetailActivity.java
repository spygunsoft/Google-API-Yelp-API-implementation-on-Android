package com.example.spygu.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.info_outline,
            R.drawable.photos,
            R.drawable.maps,
            R.drawable.review
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getIntent().getExtras().getString("name"));


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

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);;

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


    }

    String bookmarkStatus;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_detail, menu);

        bookmarkStatus = getIntent().getExtras().getString("bookmarked");
        if(bookmarkStatus.equals("true")){
            menu.getItem(1).setIcon(R.drawable.heart_fill_white);
        }else{
            menu.getItem(1).setIcon(R.drawable.heart_outline_white);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                // User chose the "Settings" item, show the app settings UI...

                String mUrl = "https://twitter.com/intent/tweet?text="+ getIntent().getExtras().getString("name") +" located at "+getIntent().getExtras().getString("address")+". Website: ";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mUrl));
                startActivity(intent);
                return true;

            case R.id.action_favorite:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                ArrayList<PlaceCustom> favourites = new ArrayList<PlaceCustom>();

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast;


                if(sharedPreferences.getString("favouriteList",null) != null){
                    Type listType = new TypeToken<ArrayList<PlaceCustom>>(){}.getType();
                    favourites = gson.fromJson(sharedPreferences.getString("favouriteList",null),listType);
                }


                if(bookmarkStatus.equals("true")){

                    int removeIndex = -1;

                    for(int i = 0; i < favourites.size();i++)
                    {
                        if(favourites.get(i).getPlaceId().equals(getIntent().getExtras().getString("placeId"))){
                            removeIndex = i;
                        }

                    }

                    favourites.remove(removeIndex);

                    item.setIcon(R.drawable.heart_outline_white);
                    bookmarkStatus = "false";



                    toast = Toast.makeText(context, getIntent().getExtras().getString("name") + " was removed from favourites ", duration);
                }else{
                    favourites.add(new PlaceCustom(
                            getIntent().getExtras().getString("placeId"),
                            getIntent().getExtras().getString("name"),
                            getIntent().getExtras().getString("category"),
                            getIntent().getExtras().getString("address"),
                            true
                    ));



                    item.setIcon(R.drawable.heart_fill_white);
                    bookmarkStatus = "true";
                    //mPlaceList.get(getPosition()).setBookmarked(true);

                    toast = Toast.makeText(context, getIntent().getExtras().getString("name") + " was added to favourites ", duration);
                }

                //editor.remove("favouriteList");

                toast.show();
                String json = gson.toJson(favourites);
                editor.putString("favouriteList",json);
                editor.apply();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupTabIcons() {
        LinearLayout tabLinearLayoutInfo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentInfo = (TextView) tabLinearLayoutInfo.findViewById(R.id.tabContent);
        tabContentInfo.setText("  "+"INFO");
        tabContentInfo.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabContentInfo);

        LinearLayout tabLinearLayoutPhotos = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentPhotos = (TextView) tabLinearLayoutPhotos.findViewById(R.id.tabContent);
        tabContentPhotos.setText("  "+"PHOTOS");
        tabContentPhotos.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabContentPhotos);

        LinearLayout tabLinearLayoutMap = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentMap = (TextView) tabLinearLayoutMap.findViewById(R.id.tabContent);
        tabContentMap.setText("  "+"MAP");
        tabContentMap.setCompoundDrawablesWithIntrinsicBounds(tabIcons[2], 0, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabContentMap);

        LinearLayout tabLinearLayoutReviews = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentReviews = (TextView) tabLinearLayoutReviews.findViewById(R.id.tabContent);
        tabContentReviews.setText("  "+"REVIEWS");
        tabContentReviews.setCompoundDrawablesWithIntrinsicBounds(tabIcons[3], 0, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabContentReviews);


    }

    private void setupViewPager(ViewPager viewPager) {
        PlaceDetailActivity.ViewPagerAdapter adapter = new PlaceDetailActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabInfoFragment(), "INFO");
        adapter.addFragment(new TabPhotosFragment(), "PHOTOS");
        adapter.addFragment(new TabMapFragment(), "MAP");
        adapter.addFragment(new TabReviewsFragment(), "REVIEWS");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
