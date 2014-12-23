package com.quanteq.badmus.pollingunitapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by badmus on 12/21/14.
 */
public class NearByPolls extends Activity {
    //public static Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.near_by_polls);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = actionBar.newTab().setText("List View");
        ActionBar.Tab tab2 = actionBar.newTab().setText("Map View");

        Fragment Fragment1Name = new ListNearByPU();
        Fragment Fragment2Name = new PUNearByMapView();

        tab1.setTabListener(new MyTabsListener(Fragment1Name));
        tab2.setTabListener(new MyTabsListener(Fragment2Name));

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
    }

    class MyTabsListener implements ActionBar.TabListener {
        public Fragment fragment;

        public MyTabsListener(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            //do what you want when tab is reselected, I do nothing
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_placeholder, fragment);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }
    }
}