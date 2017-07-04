package com.tripoffbeat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity used to select the filters
 */

public class OptionList extends AppCompatActivity implements View.OnClickListener{

    Spinner states, min_budget, max_budget, activity, dist;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Button searchHotel;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listActivity = new ArrayList<>();
    ArrayAdapter<String> adapter1, adapter2;
    private static final String TAG = "OptionList";
    Intent i;
    CheckBox states_box, budget_box, activity_box, dist_box;
    String empty = "Empty";
    RadioGroup r;
    RadioButton dist_delkm;
    RadioButton time_del;
    List<String> t = null, km = null;
    Sessions sessions;
    Boolean bool;
    TextView max_txt, min_txt;
    View parentLayout;
    Double lat, lng;
    GPSTracker mygps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout defined in activity_option_list
        setContentView(R.layout.activity_option_list);
        parentLayout = findViewById(android.R.id.content);
        //Sessions object
        sessions = new Sessions(getApplicationContext());
        i = getIntent();
        //retrieves login true or false
        bool = sessions.getVal();
        //if bool is true it will check once whether you are logged in
        //if bool is false it will not check as the box was not ticked
        //bool is true only when you check the keep me logged in box in the previous activity
        if(bool.equals(true)) {
            sessions.checkLogin();
        }
        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OptionList.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // Write you code here if permission already given.
                mygps = new GPSTracker(OptionList.this);
                if(mygps.canGetLocation()){

                    lat = mygps.getLatitude();
                    lng = mygps.getLongitude();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    mygps.showSettingsAlert();
                }
            }
        }
        catch(Exception j){
            j.printStackTrace();
        }
        new SendLoc1().execute();
        //reference to all variables
        min_txt = (TextView) findViewById(R.id.min_txt);
        max_txt = (TextView) findViewById(R.id.max_txt);
        min_txt.setVisibility(View.GONE);
        max_txt.setVisibility(View.GONE);
        searchHotel = (Button) findViewById(R.id.searchHotel);
        searchHotel.setOnClickListener(this);
        i = getIntent();
        //radio group
        r = (RadioGroup) findViewById(R.id.r);
        //function used to make the a widget invisible
        r.setVisibility(View.GONE);
        dist_delkm = (RadioButton) findViewById(R.id.dist_delkm);
        time_del = (RadioButton) findViewById(R.id.time_del);

        states_box = (CheckBox) findViewById(R.id.states_box);
        budget_box = (CheckBox) findViewById(R.id.budget_box);
        activity_box = (CheckBox) findViewById(R.id.activity_box);
        dist_box = (CheckBox) findViewById(R.id.dist_box);

        states = (Spinner) findViewById(R.id.states);
        states.setVisibility(View.GONE);
        if(listItems.isEmpty()) {
            //putting values into the state spinner
            //spinner.xml defines the spinners contents
            //listItems contains the values of all the states retrieved from the DB
            adapter1 = new ArrayAdapter<String>(this, R.layout.spinner, R.id.states, listItems);
        }

        states_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //if state box is checked it will make the takes spinner visible
                    states.setVisibility(View.VISIBLE);
                    states.setAdapter(adapter1);
                    states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                            //displays message with selected item
                            Toast.makeText(OptionList.this, parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT).show();
                        }

                        public void onNothingSelected(AdapterView<?> parent){

                        }
                    });
                } else {
                    //makes states spinner invisible on de-selecting the checkbox
                    states.setVisibility(View.GONE);
                }
            }
        });
        min_budget = (Spinner) findViewById(R.id.min_budget);
        min_budget.setVisibility(View.GONE);
        max_budget = (Spinner) findViewById(R.id.max_budget);
        max_budget.setVisibility(View.GONE);
        budget_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //same case as before but with budget checkbox
                    min_budget.setVisibility(View.VISIBLE);
                    max_budget.setVisibility(View.VISIBLE);
                    min_txt.setVisibility(View.VISIBLE);
                    max_txt.setVisibility(View.VISIBLE);
                    min_budget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(OptionList.this, parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    max_budget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(OptionList.this, parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    min_budget.setVisibility(View.GONE);
                    max_budget.setVisibility(View.GONE);
                    min_txt.setVisibility(View.GONE);
                    max_txt.setVisibility(View.GONE);
                }
            }
        });
        activity = (Spinner) findViewById(R.id.activity);
        activity.setVisibility(View.GONE);
        if(listActivity.isEmpty()) {
            //putting values into the activities spinner
            //spinner.xml defines the spinners contents
            //listActivity contains the values of all the activities retrieved from the DB
            adapter2 = new ArrayAdapter<String>(this, R.layout.spinner, R.id.activity, listActivity);
        }
        activity_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    activity.setVisibility(View.VISIBLE);
                    activity.setAdapter(adapter2);
                    activity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                            Toast.makeText(OptionList.this, parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT).show();
                        }

                        public void onNothingSelected(AdapterView<?> parent){

                        }
                    });
                } else {
                    activity.setVisibility(View.GONE);
                    //listActivity.clear();
                }
            }
        });

        dist = (Spinner) findViewById(R.id.dist_del);
        dist.setVisibility(View.GONE);

        dist_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    //once the distance check box is checked it will display two radio buttons with either distnace or time
                    r.setVisibility(View.VISIBLE);
                    dist_delkm.setChecked(true);
                    km = new ArrayList<String>();
                    km.add("100");
                    km.add("200");
                    km.add("300");
                    km.add("400");
                    km.add("500");
                    km.add("600");
                    km.add("700");
                    km.add("800");
                    km.add("900");
                    km.add("1000");
                    //ArrayAdapter used to set value into the spinner
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(OptionList.this, android.R.layout.simple_dropdown_item_1line, km);
                    dist.setAdapter(ad);
                    dist.setVisibility(View.VISIBLE);
                    r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                            switch(checkedId){
                                case R.id.dist_delkm:
                                    //If the distance radio button is selected then ArrayList km is set in the spinner
                                    km = new ArrayList<String>();
                                    km.add("100");
                                    km.add("200");
                                    km.add("300");
                                    km.add("400");
                                    km.add("500");
                                    km.add("600");
                                    km.add("700");
                                    km.add("800");
                                    km.add("900");
                                    km.add("1000");
                                    ArrayAdapter<String> ad = new ArrayAdapter<String>(OptionList.this, android.R.layout.simple_dropdown_item_1line, km);
                                    dist.setAdapter(ad);
                                    dist.setVisibility(View.VISIBLE);
                                    dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                    break;

                                case R.id.time_del:
                                    //If time is selected then the spinner will have ArrayList t
                                    t = new ArrayList<String>();
                                    t.add("1");
                                    t.add("2");
                                    t.add("3");
                                    t.add("4");
                                    t.add("5");
                                    t.add("6");
                                    t.add("7");
                                    t.add("8");
                                    t.add("9");
                                    t.add("10");
                                    t.add("11");
                                    t.add("12");
                                    ArrayAdapter<String> ad1 = new ArrayAdapter<String>(OptionList.this, android.R.layout.simple_dropdown_item_1line, t);
                                    dist.setAdapter(ad1);
                                    dist.setVisibility(View.VISIBLE);
                                    dist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                    break;

                            }
                        }
                    });
                }
                else{
                    //on un-checking the box it will clear it the radio button selection
                    r.clearCheck();
                    Toast.makeText(getApplicationContext(), "Selection cleared", Toast.LENGTH_SHORT).show();
                    r.setVisibility(View.GONE);
                    dist.setVisibility(View.GONE);
                }
            }
        });

        Log.i(TAG, "onCreate()");

    }

    @Override
    protected void onStart() {
        super.onStart();
        //retrieve data on the start of the activity for the states and activities spinner
        BackTask bt = new BackTask();
        bt.execute();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_logout:
                //Gives logout pop up
                Intent i = new Intent(getApplicationContext(), Popup.class);
                /*i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_LONG).show();*/
                startActivity(i);
                return true;

            case R.id.action_prev:
                //Search previous quotes on a browser
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("http://139.59.34.30/quotation/"));
                listActivity.clear();
                listItems.clear();
                startActivity(in);
                return true;

            case R.id.action_clear:
                //Clears all the selected fields
                if(states_box.isChecked() || budget_box.isChecked() || activity_box.isChecked() || dist_box.isChecked()) {
                    if (states_box.isChecked()) {
                        states_box.setChecked(false);
                    }
                    if (budget_box.isChecked()) {
                        budget_box.setChecked(false);
                    }
                    if (activity_box.isChecked()) {
                        activity_box.setChecked(false);
                    }
                    if (dist_box.isChecked()) {
                        dist_box.setChecked(false);
                        r.clearCheck();
                    }
                    Snackbar.make(parentLayout, "All fields cleared", Snackbar.LENGTH_SHORT).show();
                } else{
                    Snackbar.make(parentLayout, "No fields selected", Snackbar.LENGTH_SHORT).show();
                }
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.searchHotel:
                //On pressing search it will check which all checkboxes were checked
                //if one was checked it will put that selected value of the that checkbox in an intent
                //else it will put empty and pass it on to the next activity
               Intent in = new Intent(OptionList.this, Result.class);
               if(states_box.isChecked()){
                   in.putExtra("states", states.getSelectedItem().toString());
               }
               else{
                   in.putExtra("states", empty);
               }
               if(budget_box.isChecked()){
                   in.putExtra("min_budget", min_budget.getSelectedItem().toString());
                   in.putExtra("max_budget", max_budget.getSelectedItem().toString());
               }
               else{
                   in.putExtra("min_budget", empty);
                   in.putExtra("max_budget", empty);
               }
               if(activity_box.isChecked()){
                   in.putExtra("activity", activity.getSelectedItem().toString());
               }
               else{
                   in.putExtra("activity", empty);
               }
               if(dist_delkm.isChecked()){
                   in.putExtra("distance", dist.getSelectedItem().toString());
                   in.putExtra("time", empty);
               }
               else if(time_del.isChecked()){
                   in.putExtra("time", dist.getSelectedItem().toString());
                   in.putExtra("distance", empty);
               }
               else{
                   in.putExtra("distance", empty);
                   in.putExtra("time", empty);
               }
               if(!states_box.isChecked() && !budget_box.isChecked() && !activity_box.isChecked() && !time_del.isChecked() && !dist_delkm.isChecked()){
                   Toast.makeText(OptionList.this, "Please select at least one field.", Toast.LENGTH_SHORT).show();
                   break;
               }
               else {
                   listActivity.clear();
                   listItems.clear();
                   startActivity(in);
                   break;
               }

            default:
                break;
        }
    }

    private class SendLoc1 extends AsyncTask<String, String, String>{

        HttpURLConnection conn1;
        URL url1;

        @Override
        protected String doInBackground(String... params) {
            try{
                url1 = new URL("http://139.59.34.30/send_loc.php");
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn1 = (HttpURLConnection)url1.openConnection();
                conn1.setReadTimeout(READ_TIMEOUT);
                conn1.setConnectTimeout(CONNECTION_TIMEOUT);
                conn1.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn1.setDoInput(true);
                conn1.setDoOutput(true);

                String l1 = Double.toString(lat);
                String l2 = Double.toString(lng);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("latitude", l1).appendQueryParameter("longitude", l2);
                String query = builder.build().getEncodedQuery();
                Log.d("QUERY: ", query);

                // Open connection for sending data
                OutputStream os = conn1.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn1.connect();

            } catch(Exception f){
                f.printStackTrace();
                return "exc";
            }try {

                int response_code = conn1.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn1.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return "unsuccessful";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exc";
            } finally {
                conn1.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("true")) {
                Log.d("Location sent", "server");
            } else if (result.equals("false")) {
                Log.d("Location not sent", "server");
            } else if(result.equals("exc")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OptionList.this);

                        // Setting Dialog Title
                        alertDialog.setTitle("Location settings");

                        // Setting Dialog Message
                        alertDialog.setMessage("Location is not enabled. Please enable it in the settings menu to use the app.");

                        // On pressing Settings button
                        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                OptionList.this.startActivity(intent);
                            }
                        });

                        alertDialog.setCancelable(false);

                        // Showing Alert Message
                        alertDialog.show();
                    }
                });
            }
        }
    }

    private class BackTask extends AsyncTask<Void,Void,Void> {
        //class used to get data from the DB
        ArrayList<String> list;
        ArrayList<String> list2;
        protected void onPreExecute(){
            super.onPreExecute();
            list=new ArrayList<>();
            list2=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is = null;
            InputStream is1 = null;
            String result="";
            String result1="";
            try{
                HttpClient httpclient = new DefaultHttpClient();
                //Post method for the url mentioned to get all states
                HttpPost httppost= new HttpPost("http://139.59.34.30/get_states.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                // Get our response as a String.
                HttpClient httpclient1 = new DefaultHttpClient();
                //Post method for the url mentioned to get all activities
                HttpPost httppost1 = new HttpPost("http://139.59.34.30/get_activities.php");
                HttpResponse response1 = httpclient1.execute(httppost1);
                HttpEntity entity1 = response1.getEntity();
                is1 = entity1.getContent();
            }catch(IOException e){
                e.printStackTrace();
            }

            //convert response to string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result+=line;
                    Log.d("Result: ", result);
                }
                is.close();

                BufferedReader reader1 = new BufferedReader(new InputStreamReader(is1,"utf-8"));
                String line1 = null;
                while ((line1 = reader1.readLine()) != null) {
                    result1+=line1;
                }
                is1.close();
                //result=sb.toString();
            }catch(Exception e){
                e.printStackTrace();
            }
            // parse json data
            try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0; i<jArray.length(); i++){
                    JSONObject jsonObject=jArray.getJSONObject(i);
                    //add the value of the state name to a temp ArrayList
                    list.add(jsonObject.getString("name"));
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
            try{
                JSONArray jArray1 = new JSONArray(result1);
                for(int i=0; i<jArray1.length(); i++) {
                    JSONObject jsonObject = jArray1.getJSONObject(i);
                    //add the value of the activity name to a temp ArrayList
                    list2.add(jsonObject.getString("activity_name"));
                }
            }
            catch(JSONException e){
                    e.printStackTrace();
                }
            return null;
        }
        protected void onPostExecute(Void result){
            //To avoid duplicates it will check if the the lists are empty
            //if they are it will add all fetched values and insert them into the list
            //to display on the spinner
            if(listItems.isEmpty()) {
                listItems.addAll(list);
                adapter1.notifyDataSetChanged();
            }
            if(listActivity.isEmpty()) {
                listActivity.addAll(list2);
                adapter2.notifyDataSetChanged();
            }
        }
    }
}

