package com.example.spygu.hw9;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.search,
            R.drawable.heart_outline_white
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);;

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void setupTabIcons() {
        LinearLayout tabLinearLayoutSearch = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentSearch = (TextView) tabLinearLayoutSearch.findViewById(R.id.tabContent);
        tabContentSearch.setText("  "+"SEARCH");
        tabContentSearch.setCompoundDrawablesWithIntrinsicBounds(tabIcons[0], 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabContentSearch);

        LinearLayout tabLinearLayoutFavorites = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView tabContentFavorites = (TextView) tabLinearLayoutFavorites.findViewById(R.id.tabContent);
        tabContentFavorites.setText("  "+"FAVORITES");
        tabContentFavorites.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabContentFavorites);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabSearchFragment(), "SEARCH");
        adapter.addFragment(new TabFavouritesFragment(), "FAVORITES");
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
