package com.tripoffbeat;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result extends ListActivity {

    ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONparser jParser = new JSONparser();


    // url to get all products list
    private static String url_all_resorts = "http://139.59.34.30/get_lulz.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_RESORT = "resorts";
    private static final String TAG_PRICE = "room_prices";
    private static final String TAG_NAME = "resort_name";
    private static final String TAG_STATE = "name";
    private static final String TAG_CITIES = "cities";
    private static final String TAG_DIST = "dist_del";
    private static final String TAG_ROOM_NAME = "room_name";
    public static final String TAG = "Result";
    String name, price, s, c, dd, dist_delkm, time_del, rn;
    ActionBar actionBar;

    // products JSONArray
    JSONArray resorts = null;
    JSONArray rates = null;
    JSONArray sname = null;
    JSONArray cities = null;
    JSONArray dist_del = null;
    JSONArray room_name = null;

    RadioButton sort_price;
    RadioButton sort_alpha, sort_cities, sort_dist, sort_state;
    RadioGroup rad;

    ArrayList<HashMap<String, String>> resortsList;
    List<String> stringList = new ArrayList<>();
    List<String> priceList= new ArrayList<>();
    List<String> roomTypeList = new ArrayList<>();
    private SelectionAdapter mAdapter;
    View parentLayout;
    ListView lv;

    Intent in;

    String states, min_budget, max_budget, activity;

    String res_name, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_resorts);
        parentLayout = findViewById(android.R.id.content);

        new LoadAllResorts().execute();
        // Hashmap for ListView
        resortsList = new ArrayList<HashMap<String, String>>();
        rad = (RadioGroup) findViewById(R.id.rad);
        sort_price = (RadioButton) findViewById(R.id.sort_price);
        sort_alpha = (RadioButton) findViewById(R.id.sort_alpha);
        sort_cities = (RadioButton) findViewById(R.id.sort_city);
        sort_state = (RadioButton) findViewById(R.id.sort_states);
        sort_dist = (RadioButton) findViewById(R.id.sort_dist);

        in = getIntent();
        states = in.getStringExtra("states");
        min_budget = in.getStringExtra("min_budget");
        max_budget = in.getStringExtra("max_budget");
        activity = in.getStringExtra("activity");
        dist_delkm = in.getStringExtra("distance");
        time_del = in.getStringExtra("time");
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //restoreData();
        lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                res_name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                d = ((TextView) view.findViewById(R.id.distance_from_delhi)).getText().toString();
                Bundle data = saveData();
                Intent in = new Intent(Result.this, ResortDetails.class);
                in.putExtra(TAG_NAME, res_name);
                in.putExtra(TAG_DIST, d);
                in.putExtras(data);
                startActivityForResult(in, 1);
            }
        });
        mAdapter = new SelectionAdapter(this, R.layout.activity_result, R.id.name, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME});
        setListAdapter(mAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;
            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.email:
                        String room_type;
                        String [] mail_list = new String[stringList.size()];
                        String [] room_list = new String[roomTypeList.size()];
                        String [] room_price_list = new String[priceList.size()];
                        for(int i = 0; i<stringList.size() & i<roomTypeList.size() & i<priceList.size(); i++){
                            mail_list[i] = stringList.get(i);
                            room_list[i] = roomTypeList.get(i);
                            room_price_list[i] = priceList.get(i);
                            Log.d("LIST1: ", mail_list[i]);
                            Log.d("LIST2: ", room_list[i]);
                            Log.d("LIST3: ", room_price_list[i]);
                        }
                        Bundle b = new Bundle();
                        b.putStringArray("mail_list", mail_list);
                        b.putStringArray("room_list", room_list);
                        b.putStringArray("room_price_list", room_price_list);
                        Bundle data = saveData();
                        Log.d("Data: ", data.toString());
                        in = new Intent(Result.this, mail.class);
                        in.putExtras(b);
                        in.putExtras(data);
                        startActivityForResult(in, 1);
                        break;


                    default:
                        break;

                }
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                mAdapter.clearSelection();
                stringList.clear();
            }

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    nr++;
                    mAdapter.setNewSelection(position, checked);
                    String get_list = resortsList.get(position).get(TAG_NAME);
                    String get_price = resortsList.get(position).get(TAG_PRICE);
                    String room_type = resortsList.get(position).get(TAG_ROOM_NAME);
                    stringList.add(get_list);
                    priceList.add(get_price);
                    roomTypeList.add(room_type);
                    Log.d("String List: ", stringList.toString());
                    Log.d("String List: ", priceList.toString());
                    Log.d("String List: ", roomTypeList.toString());
                } else {
                    nr--;
                    mAdapter.removeSelection(position);
                    int i;
                    for(i = 0 ; i < stringList.size(); i++){
                        if(stringList.get(i).equals(resortsList.get(position).get(TAG_NAME)) && priceList.get(i).equals(resortsList.get(position).get(TAG_PRICE)) && roomTypeList.get(i).equals(resortsList.get(position).get(TAG_ROOM_NAME))){
                            stringList.remove(i);
                            priceList.remove(i);
                            roomTypeList.remove(i);
                            Log.d("String List: ", stringList.toString());
                            Log.d("String List: ", priceList.toString());
                            Log.d("String List: ", roomTypeList.toString());
                            break;
                        }
                    }
                    Log.d("String List: ", stringList.toString());
                }
                mode.setTitle("Resorts selected: " + nr);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                getListView().setItemChecked(position, !mAdapter.isPositionChecked(position));
                return true;
            }
        });
        Log.i(TAG, "onCreate()");
        rad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.sort_alpha:
                        Collections.sort(resortsList, new MapComparator(TAG_NAME));
                        Log.d("List alpha: ", resortsList.toString());
                        ListAdapter adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type});
                        // updating listview
                        setListAdapter(adapter);
                        break;

                    case R.id.sort_price:
                        Collections.sort(resortsList, new MapComparator(TAG_PRICE));
                        Log.d("List price: ", resortsList.toString());
                        ListAdapter adapter1 = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type});
                        // updating listview
                        setListAdapter(adapter1);
                        break;

                    case R.id.sort_states:
                        Collections.sort(resortsList, new MapComparator(TAG_STATE));
                        Log.d("List state: ", resortsList.toString());
                        ListAdapter adapter2 = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type});
                        // updating listview
                        setListAdapter(adapter2);
                        break;

                    case R.id.sort_city:
                        Collections.sort(resortsList, new MapComparator(TAG_CITIES));
                        Log.d("List alpha: ", resortsList.toString());
                        ListAdapter adapter3 = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type});
                        // updating listview
                        setListAdapter(adapter3);
                        break;

                    case R.id.sort_dist:
                        Collections.sort(resortsList, new MapComparator(TAG_DIST));
                        Log.d("List alpha: ", resortsList.toString());
                        ListAdapter adapter4 = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type});
                        // updating listview
                        setListAdapter(adapter4);
                        break;

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                restoreData();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    private class MapComparator implements Comparator<Map<String, String>> {
        private final String key;

        MapComparator(String key){
            this.key = key;
        }

        public int compare(Map<String, String> first,
                           Map<String, String> second){
            // TODO: Null checking, both for maps and values
            String firstValue = first.get(key);
            String secondValue = second.get(key);
            return firstValue.compareTo(secondValue);
        }
    }

    private class SelectionAdapter extends ArrayAdapter<String> {

        private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

        SelectionAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }

        boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }

        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }

        void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }

        void clearSelection() {
            mSelection = new HashMap<Integer, Boolean>();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            v.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color

            if (mSelection.get(position) != null) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }
            return v;
        }
    }

    private Bundle saveData() {
        Log.d(TAG, "Started saving state");
        Bundle data = new Bundle();

        data.putString(states, in.getStringExtra("states"));
        data.putString(min_budget, in.getStringExtra("min_budget"));
        data.putString(max_budget, in.getStringExtra("max_budget"));
        data.putString(activity, in.getStringExtra("activity"));
        return data;
    }

    private void restoreData() {
        Log.d(TAG, "Started restoring state");

        Bundle data = getIntent().getExtras();
        // restore stuff in the bundle
        states = data.getString(states);
        min_budget = data.getString(min_budget);
        max_budget = data.getString(max_budget);
        activity = data.getString(activity);
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

    private class LoadAllResorts extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Result.this);
            pDialog.setMessage("Loading resorts. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args){

            // Check your log cat for JSON response
            JSONObject json = null;
            try {
                if(states.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest3(url_all_resorts, activity);
                }
                else if(states.equals("Empty") && activity.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")) {
                    json = jParser.makeHttpRequest4(url_all_resorts, min_budget, max_budget);
                }
                else if (min_budget.equals("Empty") && max_budget.equals("Empty") && activity.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")) {
                    json = jParser.makeHttpRequest5(url_all_resorts, states);
                }
                else if(states.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && activity.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest15(url_all_resorts, time_del);
                }
                else if(states.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && activity.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest16(url_all_resorts, dist_delkm);
                }
                else if( min_budget.equals("Empty") && max_budget.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest6(url_all_resorts, states, activity);
                }
                else if(states.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest7(url_all_resorts, min_budget, max_budget, activity);
                }
                else if(activity.equals("Empty") && dist_delkm.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest8(url_all_resorts, states, min_budget, max_budget);
                }
                else if(activity.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest10(url_all_resorts, states, time_del);
                }
                else if(activity.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest11(url_all_resorts, states, dist_delkm);
                }
                else if(states.equals("Empty") && activity.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest12(url_all_resorts, min_budget, max_budget, time_del);
                }
                else if(states.equals("Empty") && activity.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest13(url_all_resorts, min_budget, max_budget, dist_delkm);
                }
                else if(states.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest25(url_all_resorts, activity, time_del);
                }
                else if(states.equals("Empty") && min_budget.equals("Empty") && max_budget.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest14(url_all_resorts, activity, dist_delkm);
                }
                else if(activity.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest17(url_all_resorts, states, min_budget, max_budget, dist_delkm);
                }
                else if(activity.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest18(url_all_resorts, states, min_budget, max_budget, time_del);
                }
                else if(states.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest19(url_all_resorts, min_budget, max_budget, activity, dist_delkm);
                }
                else if(states.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest20(url_all_resorts, min_budget, max_budget, activity, time_del);
                }
                else if(min_budget.equals("Empty") && max_budget.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest21(url_all_resorts, states, activity, dist_delkm);
                }
                else if(min_budget.equals("Empty") && max_budget.equals("Empty") && dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest22(url_all_resorts, states, activity, time_del);
                }
                else if(dist_delkm.equals("Empty") && time_del.equals("Empty")){
                    json = jParser.makeHttpRequest(url_all_resorts, states, min_budget, max_budget, activity);
                }
                else if(time_del.equals("Empty")){
                    json = jParser.makeHttpRequest23(url_all_resorts, states, min_budget, max_budget, activity, dist_delkm);
                }
                else if(dist_delkm.equals("Empty")){
                    json = jParser.makeHttpRequest24(url_all_resorts, states, min_budget, max_budget, activity, time_del);
                }

                Log.d("All Resorts : ", json.toString());
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // resorts found
                    // Getting Array of resorts
                    resorts = json.getJSONArray(TAG_RESORT);
                    rates = json.getJSONArray(TAG_PRICE);
                    sname = json.getJSONArray(TAG_STATE);
                    cities = json.getJSONArray(TAG_CITIES);
                    dist_del = json.getJSONArray(TAG_DIST);
                    room_name = json.getJSONArray(TAG_ROOM_NAME);

                    int i;

                    // looping through All resorts
                    for (i = 0; i < resorts.length() & i < rates.length() & i < sname.length() & i < cities.length() & i < dist_del.length() & i < room_name.length(); i++) {
                        //JSONObject c = resorts.getJSONObject(i);
                        //JSONObject d = rates.getJSONObject(i);

                        // Storing each json item in variable
                        name = resorts.getString(i);
                        price = rates.getString(i);
                        s = sname.getString(i);
                        c = cities.getString(i);
                        dd = dist_del.getString(i);
                        rn = room_name.getString(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_STATE, s);
                        map.put(TAG_CITIES, c);
                        map.put(TAG_DIST, dd);
                        map.put(TAG_ROOM_NAME, rn);

                        // adding HashList to ArrayList
                        resortsList.add(map);
                        Log.d("Details: ", resortsList.toString());
                    }

                } else {
                    // no resorts found
                    pDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Result.this ,"No resorts found", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                pDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Result.this ,"Error connecting to server", Toast.LENGTH_LONG).show();
                    }
                });
            } catch(NullPointerException e){
                pDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Result.this ,"Error: please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all resorts
            pDialog.dismiss();

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type});
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }

    }
}






