package com.quanteq.badmus.pollingunitapp;

/**
 * Created by badmus on 12/17/14.
 */
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListNearByPU extends Fragment
{
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

/*@Override
public void onActivityCreated(Bundle savedInstanceState)
{
    super.onActivityCreated(savedInstanceState);
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
            android.R.layout.simple_list_item_1, list_items);
        setListAdapter(adapter);
}*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearpulayout, container, false);
        createListView(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void createListView(View view)
    {
        PUList = new ArrayList<HashMap<String, String>>();
        lv = (ListView)view.findViewById(R.id.PUNearListView);
        new GetPollingUnits().execute();

        /*lView1 = (ListView) view.findViewById(R.id.PUNearListView);
        //Set option as Multiple Choice. So that user can able to select more the one option from list
        lView1.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list_items));
        ListAdapter adapter = new SimpleAdapter(
                ListNearByPU.this, PUList,
                R.layout.purowlayout, new String[]{TAG_ID, TAG_DESC,}, new int[]{R.id.PUID_NearByList,
                R.id.Location_NearByList,});

        lv.setAdapter(adapter);*/
    }

    private class GetPollingUnits extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Polling Units...");
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
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), PUList,
                    R.layout.purowlayout, new String[]{TAG_ID, TAG_DESC,}, new int[]{R.id.PUID_NearByList,
                    R.id.Location_NearByList,});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                    Intent i = new Intent(getActivity(), PollingUnitDetails.class);
                    startActivity(i);

                }
            });
        }
    }
}