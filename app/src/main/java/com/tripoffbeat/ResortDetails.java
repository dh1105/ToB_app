package com.tripoffbeat;

import android.app.ActionBar;
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
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;

public class ResortDetails extends AppCompatActivity implements  View.OnClickListener, OnMapReadyCallback {

    private static String res_details = "http://139.59.34.30/get_details.php";
    private static final String TAG_RES_DES = "resort_desc";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_TIME = "time";
    public static final String TAG = "ResortDetails";
    double latitude;
    double longitude;
    private GoogleMap mMap;
    Intent i;
    String res_name, d;
    Button back;
    // Progress Dialog
    ProgressDialog pDialog;
    TextView res_n, description, dist, time;
    View parentLayout;

    // Creating JSON Parser object
    JSONparser jParser = new JSONparser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resort_details);
        parentLayout = findViewById(android.R.id.content);
        res_n = (TextView) findViewById(R.id.res_n);
        description = (TextView) findViewById(R.id.description);
        dist = (TextView) findViewById(R.id.dist);
        time = (TextView) findViewById(R.id.time);
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);
        new GetResortDetails().execute();
        i = getIntent();
        res_name = i.getStringExtra("resort_name");
        d = i.getStringExtra("dist_del");

        dist.setText(d);
        res_n.setText(res_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Bundle data = getIntent().getExtras();
                i = new Intent(ResortDetails.this, Result.class);
                i.putExtras(data);
                setResult(RESULT_OK, getIntent());
                finish();
                break;

            default:
                break;
        }
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
                JSONObject json = jParser.makeHttpRequest1(res_details, res_name);
                Log.d("Details : ", json.toString());
                int s = json.getInt(TAG_SUCCESS);

                if (s == 1){
                    JSONArray a = json.getJSONArray(TAG_RES_DES);
                    JSONArray b = json.getJSONArray(TAG_LAT);
                    JSONArray c = json.getJSONArray(TAG_LONG);
                    JSONArray d = json.getJSONArray(TAG_TIME);
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
                Snackbar.make(parentLayout, "Error: please check your internet connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle data = getIntent().getExtras();
                                i = new Intent(ResortDetails.this, Result.class);
                                i.putExtras(data);
                                setResult(RESULT_OK, getIntent());
                                finish();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                        .show();
                } catch(JSONException f){
                pDialog.dismiss();
                f.printStackTrace();
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle data = getIntent().getExtras();
                                i = new Intent(ResortDetails.this, Result.class);
                                i.putExtras(data);
                                setResult(RESULT_OK, getIntent());
                                finish();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                        .show();
            }

                return null;
            }

            protected void onPostExecute(String file_url) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        description.setText(des);
                        time.setText(t);
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
        mMap = googleMap;
        if( mMap != null){
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
        } else {
            Toast.makeText(this, "Error - Map was null", Toast.LENGTH_SHORT).show();
        }

    }
}

