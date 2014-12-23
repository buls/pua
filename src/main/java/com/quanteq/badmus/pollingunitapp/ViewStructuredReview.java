package com.quanteq.badmus.pollingunitapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by badmus on 12/19/14.
 */
public class ViewStructuredReview extends Activity {

    private ProgressDialog pDialog;

    private String url;
    // JSON Node names
    private static final String TAG_PUID = "payload";
    private static final String TAG_ID = "locationId";
    private static final String TAG_DESC = "desc";

    // Polling Units JSONArray
    JSONArray PollingUnits, PollingUnit = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> PUList;
    ListView lv;

    private static final String TAG = "Debug";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_structured_review);


        PUList = new ArrayList<HashMap<String, String>>();
        lv = (ListView)findViewById(R.id.PUReviewListView);
        new GetPollingUnits().execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetPollingUnits extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ViewStructuredReview.this);
            pDialog.setMessage("Loading Polling Unit's Structured Review...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            url = "http://pua-2015.appspot.com/pua?a=locationpus&lt=8.500051256&ln=4.5500114949";
            Log.d(TAG, "URL in background: " + url);
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    PollingUnits = jsonObj.getJSONArray(TAG_PUID);
                    PollingUnit = PollingUnits.getJSONArray(0);
                    // looping through All Contacts
                    for (int i = 0; i < PollingUnit.length(); i++) {
                        JSONObject c = PollingUnit.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_DESC);

                        // tmp hashmap for single contact
                        HashMap<String, String> PUnit = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        PUnit.put(TAG_ID, id);
                        PUnit.put(TAG_DESC, name);

                        // adding contact to contact list
                        PUList.add(PUnit);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             **/
            ListAdapter adapter = new SimpleAdapter(
                    ViewStructuredReview.this, PUList,
                    R.layout.pu_review_row_layout, new String[]{TAG_ID, TAG_DESC}, new int[]{R.id.SelectedStructuredReview,
                    R.id.ReviewCount});

            lv.setAdapter(adapter);
        }
    }
}