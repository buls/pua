package com.quanteq.badmus.pollingunitapp;

/**
 * Created by badmus on 12/17/14.
 */

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


public class GetCurrentLocation  extends ActionBarActivity {

    private LocationManager locationMangaer=null;
    private LocationListener locationListener=null;

    private String longitude,latitude;

    private static final String TAG = "Debug";
    private Boolean flag = false;

    public void GetMyLocation(){
        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        flag = displayGpsStatus();
        if (flag) {
            locationListener = new MyLocationListener();
            locationMangaer.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10,locationListener);
        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            longitude = "Longitude: " +loc.getLongitude();
            Log.v(TAG, longitude);
            latitude = "Latitude: " +loc.getLatitude();
            Log.v(TAG, latitude);


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}