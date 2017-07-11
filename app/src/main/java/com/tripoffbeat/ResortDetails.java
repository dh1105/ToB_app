package com.tripoffbeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity used to display the details of the resort selected
 */

public class ResortDetails extends AppCompatActivity implements OnMapReadyCallback {

    //URLs to get all details
    private static String res_details = "http://139.59.34.30/get_details.php";
    private static String get_activity_array = "http://139.59.34.30/get_activity_array.php";
    List<String> activity_list = new ArrayList<>();
    //Used to get details of all resorts from a JSONArray
    private static final String TAG_RES_DES = "resort_desc";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_TIME = "time";
    public static final String TAG = "ResortDetails";
    private static final String TAG_ACT = "activity_name";
    double latitude;
    double longitude;
    JSONArray act = null;
    private GoogleMap mMap;
    Intent i;
    String res_name, d, activity, p, r_n, ct, st, tob_rating;
    TextView act_text;
    // Progress Dialog
    ProgressDialog pDialog;
    TextView res_n, description, dist, time, price, room_type, state_name, city_name, rating;
    View parentLayout;
    Logout logout;

    // Creating JSON Parser object
    JSONparser jParser = new JSONparser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout defined in activity_resort_details
        setContentView(R.layout.activity_resort_details);
        parentLayout = findViewById(android.R.id.content);
        logout = new Logout(ResortDetails.this);
        //TextViews used to set text
        res_n = (TextView) findViewById(R.id.res_n);
        description = (TextView) findViewById(R.id.description);
        dist = (TextView) findViewById(R.id.dist);
        time = (TextView) findViewById(R.id.time);
        price = (TextView) findViewById(R.id.price);
        room_type = (TextView) findViewById(R.id.room_type);
        act_text = (TextView) findViewById(R.id.act_text);
        state_name = (TextView) findViewById(R.id.state_name);
        city_name = (TextView) findViewById(R.id.city_name);
        rating = (TextView) findViewById(R.id.tob_rate);
        //Executing the task of getting description, lat, long, activities, time from DB
        new GetResortDetails().execute();
        i = getIntent();
        //Getting values from previous activity
        res_name = i.getStringExtra("resort_name");
        d = i.getStringExtra("dist_del");
        p = i.getStringExtra("room_prices");
        r_n = i.getStringExtra("room_name");
        ct = i.getStringExtra("name");
        st = i.getStringExtra("cities");
        tob_rating = i.getStringExtra("rating");
        //Setting all values recieved from previous activity onto textviews
        dist.setText(d);
        res_n.setText(res_name);
        price.setText(p);
        room_type.setText(r_n);
        state_name.setText(ct);
        city_name.setText(st);
        rating.setText(tob_rating);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_logout:
                logout.showLogout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pDialog.dismiss();
        Log.i(TAG, "onDestroy()");
    }


    private class GetResortDetails extends AsyncTask<String, String, String> {

        /**
         * Class used to get values
         */

        String des, t;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResortDetails.this);
            pDialog.setMessage("Loading details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Check your log cat for JSON response
            try {
                //method with parameters attached
                JSONObject json = jParser.makeHttpRequest1(res_details, res_name);
                Log.d("Details : ", json.toString());
                //if value recieved is 1 i.e. success it will get all the values
                int s = json.getInt(TAG_SUCCESS);

                if (s == 1){
                    //Get values defined by the Strings
                    JSONArray a = json.getJSONArray(TAG_RES_DES);
                    JSONArray b = json.getJSONArray(TAG_LAT);
                    JSONArray c = json.getJSONArray(TAG_LONG);
                    JSONArray d = json.getJSONArray(TAG_TIME);
                    //Putting values into the variables
                    des = a.getString(0);
                    latitude = b.getDouble(0);
                    longitude = c.getDouble(0);
                    t = d.getString(0);
                    Log.d("Lat: ", Double.toString(latitude));
                    Log.d("Lon: ", Double.toString(longitude));
                }
                }catch(NullPointerException e){
                pDialog.dismiss();
                e.printStackTrace();
                //Internet problem warning
                Snackbar.make(parentLayout, "Error: please check your internet connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Will retry to get values once internet is checked
                                new GetResortDetails().execute();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                        .show();
                } catch(JSONException f){
                pDialog.dismiss();
                f.printStackTrace();
                //Problem parsing the JSON String as all values have not been received
                //Occurs when the DB does not have a value you are asking for
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Retry action if the problem persists check the DB
                                new GetResortDetails().execute();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                        .show();
            }
            try{
                //Used to get an array of activities
                JSONObject json = jParser.makeHttpRequest9(get_activity_array, res_name);
                Log.d("Details : ", json.toString());
                int s = json.getInt(TAG_SUCCESS);

                if(s==1){
                    act = json.getJSONArray(TAG_ACT);
                    for(int i = 0; i < act.length(); i++){
                        activity = act.getString(i);
                        activity_list.add(activity);
                        //activity_list has all activities
                        Log.d("List: ", activity_list.toString());
                    }
                }
            } catch (Exception f){
                pDialog.dismiss();
                f.printStackTrace();
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new GetResortDetails().execute();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark))
                        .show();
            }

                return null;
            }

            protected void onPostExecute(String file_url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        //Set the text after getting it
                        description.setText(des);
                        time.setText(t);
                        Float rate;
                        String[] act_name = new String[activity_list.size()];
                        for(int i = 0; i<activity_list.size(); i++){
                            act_name[i] = activity_list.get(i);
                            Log.d("LIST: ", act_name[i]);
                        }
                        String act_f = Arrays.toString(act_name);
                        if(act_f.contains("[") || act_f.contains("]")){
                            act_f = act_f.replace("[", " ");
                            act_f = act_f.replace("]", " ");
                        }
                        //If no activities are found
                        if(act_f.equals("") || act_f.equals(" ") || act_f.equals("  ")){
                            String nil = "No activities found";
                            act_text.setText(nil);
                        }
                        else{
                            act_text.setText(act_f);
                        }
                        //Map is loaded
                        //getMapAsync calls onMapReady
                        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        if(mapFrag != null) {
                            mapFrag.getMapAsync(ResortDetails.this);
                        } else {
                            Toast.makeText(ResortDetails.this, "Error fragment is null", Toast.LENGTH_SHORT).show();
                        }
                    }});
                pDialog.dismiss();

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        loadMap(map);
    }

    protected void loadMap(GoogleMap googleMap) {
        //Loads the map
        mMap = googleMap;
        if( mMap != null){
            if(latitude == 0 && longitude == 0){
                Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
            else {
                LatLng l = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions().position(l).title(res_name);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(l).zoom(12).build();
                Toast.makeText(this, "Map loading", Toast.LENGTH_SHORT).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(marker);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setTrafficEnabled(false);
                mMap.setIndoorEnabled(false);
                mMap.setBuildingsEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        } else {
            Toast.makeText(this, "Error - Map was null", Toast.LENGTH_SHORT).show();
        }

    }
}

