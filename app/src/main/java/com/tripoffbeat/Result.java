package com.tripoffbeat;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result extends AppCompatActivity implements SearchView.OnQueryTextListener{

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
    private static final String TAG_RATING = "rating";
    public static final String TAG = "Result";
    String name, price, s, c, dd, dist_delkm, time_del, rn, rating;
    SimpleAdapter adapter;

    // products JSONArray
    JSONArray resorts = null;
    JSONArray rates = null;
    JSONArray sname = null;
    JSONArray cities = null;
    JSONArray dist_del = null;
    JSONArray room_name = null;
    JSONArray tob_rate = null;

    //RadioButtons for sorting
    RadioButton sort_price, sort_rating;
    RadioButton sort_alpha, sort_cities, sort_dist, sort_state;
    RadioGroup rad;

    //Used to store result from DB
    ArrayList<HashMap<String, String>> resortsList;
    //Used to store result after searching
    ArrayList<HashMap<String, String>> filter_resort_list;
    List<String> stringList = new ArrayList<>();
    List<String> priceList= new ArrayList<>();
    List<String> roomTypeList = new ArrayList<>();
    //SimpleAdapter used to set up ListView
    private SelectionAdapter mAdapter;
    View parentLayout;
    ListView lv;
    ResortFilter resortFilter = new ResortFilter();

    Intent in;

    String states, min_budget, max_budget, activity;

    String res_name, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_resorts);
        parentLayout = findViewById(android.R.id.content);

        new LoadAllResorts().execute();
        resortsList = new ArrayList<HashMap<String, String>>();
        rad = (RadioGroup) findViewById(R.id.rad);
        sort_price = (RadioButton) findViewById(R.id.sort_price);
        sort_alpha = (RadioButton) findViewById(R.id.sort_alpha);
        sort_cities = (RadioButton) findViewById(R.id.sort_city);
        sort_state = (RadioButton) findViewById(R.id.sort_states);
        sort_dist = (RadioButton) findViewById(R.id.sort_dist);
        sort_rating = (RadioButton) findViewById(R.id.sort_rating);

        in = getIntent();
        //Getting values of filters from previous values
        states = in.getStringExtra("states");
        min_budget = in.getStringExtra("min_budget");
        max_budget = in.getStringExtra("max_budget");
        activity = in.getStringExtra("activity");
        dist_delkm = in.getStringExtra("distance");
        time_del = in.getStringExtra("time");
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Log.d("action bar: ", "not null");
        }

        //restoreData();
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String p, r_t, na, ct, rating;
                res_name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                d = ((TextView) view.findViewById(R.id.distance_from_delhi)).getText().toString();
                p = ((TextView) view.findViewById(R.id.price)).getText().toString();
                r_t = ((TextView) view.findViewById(R.id.room_type)).getText().toString();
                na = ((TextView) view.findViewById(R.id.cities)).getText().toString();
                ct = ((TextView) view.findViewById(R.id.states)).getText().toString();
                rating = ((TextView) view.findViewById(R.id.tob_rating)).getText().toString();
                //Bundle data = saveData();
                Intent in = new Intent(Result.this, ResortDetails.class);
                //Starting ResortDetails.class and sending values to it
                in.putExtra(TAG_NAME, res_name);
                in.putExtra(TAG_DIST, d);
                in.putExtra(TAG_ROOM_NAME, r_t);
                in.putExtra(TAG_PRICE, p);
                in.putExtra(TAG_CITIES, na);
                in.putExtra(TAG_STATE, ct);
                in.putExtra(TAG_RATING, rating);
                //in.putExtras(data);
                //startActivityForResult(in, 1);
                startActivity(in);
            }
        });
        //Setting adapter for the Contextual action bar which pops on long click
        mAdapter = new SelectionAdapter(this, R.layout.activity_result, R.id.name, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING});
        lv.setAdapter(mAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;
            //Create action bar on longclick
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

            //clicking mail will take you to next activity
            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.email:
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
                        //passing values of resort_name, room_type, room_price for a resort in a String array
                        Bundle b = new Bundle();
                        b.putStringArray("mail_list", mail_list);
                        b.putStringArray("room_list", room_list);
                        b.putStringArray("room_price_list", room_price_list);
                        //Bundle data = saveData();
                        in = new Intent(Result.this, mail.class);
                        in.putExtras(b);
                        //in.putExtras(data);
                        //startActivityForResult(in, 1);
                        startActivity(in);
                        break;


                    default:
                        break;

                }
                return false;
            }

            //clearing all arrays and clearing selection
            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {
                mAdapter.clearSelection();
                stringList.clear();
                roomTypeList.clear();
                priceList.clear();
            }

            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    //On selecting the resort we add it to an ArrayList
                    nr++;
                    mAdapter.setNewSelection(position, checked);
                    String get_list;
                    String get_price;
                    String room_type;
                    //if filter list is not null we add the details from that
                    if(filter_resort_list != null){
                        get_list = filter_resort_list.get(position).get(TAG_NAME);
                        get_price = filter_resort_list.get(position).get(TAG_PRICE);
                        room_type = filter_resort_list.get(position).get(TAG_ROOM_NAME);
                    } else {
                        get_list = resortsList.get(position).get(TAG_NAME);
                        get_price = resortsList.get(position).get(TAG_PRICE);
                        room_type = resortsList.get(position).get(TAG_ROOM_NAME);
                    }
                    stringList.add(get_list);
                    priceList.add(get_price);
                    roomTypeList.add(room_type);
                    Log.d("String List: ", stringList.toString());
                    Log.d("String List: ", priceList.toString());
                    Log.d("String List: ", roomTypeList.toString());
                } else {
                    nr--;
                    mAdapter.removeSelection(position);
                    //Remove resort_name, room_price, room_type from the lists
                    int i;
                    if(filter_resort_list != null){
                        for(i = 0 ; i < stringList.size(); i++){
                            if(stringList.get(i).equals(filter_resort_list.get(position).get(TAG_NAME)) && priceList.get(i).equals(filter_resort_list.get(position).get(TAG_PRICE)) && roomTypeList.get(i).equals(filter_resort_list.get(position).get(TAG_ROOM_NAME))){
                                stringList.remove(i);
                                priceList.remove(i);
                                roomTypeList.remove(i);
                                Log.d("String List: ", stringList.toString());
                                Log.d("String List: ", priceList.toString());
                                Log.d("String List: ", roomTypeList.toString());
                                break;
                            }
                        }
                    }
                    else {
                        for (i = 0; i < stringList.size(); i++) {
                            if (stringList.get(i).equals(resortsList.get(position).get(TAG_NAME)) && priceList.get(i).equals(resortsList.get(position).get(TAG_PRICE)) && roomTypeList.get(i).equals(resortsList.get(position).get(TAG_ROOM_NAME))) {
                                stringList.remove(i);
                                priceList.remove(i);
                                roomTypeList.remove(i);
                                Log.d("String List: ", stringList.toString());
                                Log.d("String List: ", priceList.toString());
                                Log.d("String List: ", roomTypeList.toString());
                                break;
                            }
                        }
                    }
                }
                //Set title as number of resorts selected
                mode.setTitle("Resorts selected: " + nr);
            }
        });
        //Enables the contextual action bar on long click
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lv.setItemChecked(position, !mAdapter.isPositionChecked(position));
                return true;
            }
        });
        Log.i(TAG, "onCreate()");
        //Checks for a radio button change and sorts accordingly. The names of the RadioButton make it self explanatory
        rad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.sort_alpha:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_NAME));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_NAME));
                            Log.d("List alpha: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                    case R.id.sort_price:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_PRICE));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_PRICE));
                            Log.d("List price: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                    case R.id.sort_states:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_STATE));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_STATE));
                            Log.d("List state: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                    case R.id.sort_city:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_CITIES));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_CITIES));
                            Log.d("List alpha: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                    case R.id.sort_dist:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_DIST));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_DIST));
                            Log.d("List alpha: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                    case R.id.sort_rating:
                        if(filter_resort_list != null){
                            Collections.sort(filter_resort_list, new MapComparator(TAG_RATING));
                            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }
                        else {
                            Collections.sort(resortsList, new MapComparator(TAG_RATING));
                            Log.d("List alpha: ", resortsList.toString());
                            adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[]{TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[]{R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi, R.id.room_type, R.id.tob_rating});
                            // updating listview
                            lv.setAdapter(adapter);
                            break;
                        }

                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                restoreData();
            }
        }
    }*/

    /**
     * Defines the layout for the Action bar and inserts a search widget
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar1, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(Result.this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_logout:
                Intent i = new Intent(getApplicationContext(), Popup.class);
                /*i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_LONG).show();*/
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *Methods used by the search widget
     */

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Object of ResortFilter and calls the getFilter method to filter results
        resortFilter.getFilter().filter(newText);
        return true;
    }


    private class ResortFilter extends Filter{

        public Filter getFilter() {
            if(resortFilter == null){
                resortFilter = new ResortFilter();
            }
            return resortFilter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            /*Searches the resort name, state, city, room_type, distance, price fields and if the type text is present in any of the
            * forementioned fields it adds it to a temporary list*/
            if(constraint != null && constraint.length()>0){
                ArrayList<HashMap<String, String>> tempList = new ArrayList<>();
                for(HashMap<String, String> u : resortsList){
                    if(u.get(TAG_NAME).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            u.get(TAG_PRICE).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            u.get(TAG_CITIES).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            u.get(TAG_STATE).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            u.get(TAG_DIST).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            u.get(TAG_ROOM_NAME).toLowerCase().contains(constraint.toString().toLowerCase())){

                        HashMap<String, String> m = new HashMap<>();
                        m.put(TAG_NAME, u.get(TAG_NAME));
                        Log.d("Res: ", u.get(TAG_NAME));
                        m.put(TAG_PRICE, u.get(TAG_PRICE));
                        Log.d("PRice: ", u.get(TAG_PRICE));
                        m.put(TAG_CITIES, u.get(TAG_CITIES));
                        Log.d("City: ", u.get(TAG_CITIES));
                        m.put(TAG_STATE, u.get(TAG_STATE));
                        Log.d("State: ", u.get(TAG_STATE));
                        m.put(TAG_DIST, u.get(TAG_DIST));
                        Log.d("Dist: ", u.get(TAG_DIST));
                        m.put(TAG_ROOM_NAME, u.get(TAG_ROOM_NAME));
                        Log.d("Room type: ", u.get(TAG_ROOM_NAME));
                        m.put(TAG_RATING, u.get(TAG_RATING));
                        Log.d("Map: ", m.toString());

                        tempList.add(m);
                        Log.d("TempList1: ", tempList.toString());
                    }
                }
                //If the result isn't empty it resets the list size and values
                filterResults.count = tempList.size();
                filterResults.values = tempList;
                Log.d("TempList: ", tempList.toString());

            } else{
                filterResults.count = resortsList.size();
                filterResults.values = resortsList;
            }
            //return results to publishResults
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //send all values returned from the previous function and set them in the filter_resort_list
            filter_resort_list = (ArrayList<HashMap<String, String >>) results.values;
            //Sorts according to the button checked
            if (sort_price.isChecked()) {
                Collections.sort(filter_resort_list, new MapComparator(TAG_PRICE));
            }
            else if(sort_alpha.isChecked()){
                Collections.sort(filter_resort_list, new MapComparator(TAG_NAME));
            }
            else if(sort_dist.isChecked()){
                Collections.sort(filter_resort_list, new MapComparator(TAG_DIST));
            }
            else if(sort_state.isChecked()){
                Collections.sort(filter_resort_list, new MapComparator(TAG_STATE));
            }
            else if(sort_cities.isChecked()){
                Collections.sort(filter_resort_list, new MapComparator(TAG_CITIES));
            }
            else if(sort_rating.isChecked()){
                Collections.sort(filter_resort_list, new MapComparator(TAG_RATING));
            }
            adapter = new SimpleAdapter(Result.this, filter_resort_list, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
            // updating listview
            lv.setAdapter(adapter);
        }
    }

    /**
     * Class used to sort by comparing one value with another
     */

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

    /**
     * Class used to initialze the contextual action bar
     */

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

    /*private Bundle saveData() {
        Log.d(TAG, "Started saving state");
        Bundle data = new Bundle();

        data.putString(states, in.getStringExtra("states"));
        data.putString(min_budget, in.getStringExtra("min_budget"));
        data.putString(max_budget, in.getStringExtra("max_budget"));
        data.putString(activity, in.getStringExtra("activity"));
        return data;
    }*/

    /*private void restoreData() {
        Log.d(TAG, "Started restoring state");

        Bundle data = getIntent().getExtras();
        // restore stuff in the bundle
        states = data.getString(states);
        min_budget = data.getString(min_budget);
        max_budget = data.getString(max_budget);
        activity = data.getString(activity);
    }*/

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

    /**
     *Class used to get details of resorts and store it in a ArrayList<HashMap<String, String>>
     */

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
            //Based on the parameters passed it will select the method and pass certain parameters set in the previous activity
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
                    tob_rate = json.getJSONArray(TAG_RATING);

                    int i;

                    // looping through All resorts
                    for (i = 0; i < resorts.length() & i < rates.length() & i < sname.length() & i < cities.length() & i < dist_del.length() & i < room_name.length() & i < tob_rate.length(); i++) {

                        // Storing each json item in variable
                        name = resorts.getString(i);
                        price = rates.getString(i);
                        s = sname.getString(i);
                        c = cities.getString(i);
                        dd = dist_del.getString(i);
                        rn = room_name.getString(i);
                        rating = tob_rate.getString(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_STATE, s);
                        map.put(TAG_CITIES, c);
                        map.put(TAG_DIST, dd);
                        map.put(TAG_ROOM_NAME, rn);
                        map.put(TAG_RATING, rating);

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
                            Snackbar.make(parentLayout, "No resorts found", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("CLOSE", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent in = new Intent(Result.this, OptionList.class);
                                            startActivity(in);
                                            finish();
                                        }
                                    })
                                    .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                                    .show();
                            //Toast.makeText(Result.this ,"No resorts found", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (JSONException e) {
                pDialog.dismiss();
                //Problem parsing the JSON String as all values have not been received
                //Occurs when the DB does not have a value you are asking for
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Retry action if the problem persists check the DB
                                        new LoadAllResorts().execute();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                                .show();
                        //Toast.makeText(Result.this ,"Error connecting to server", Toast.LENGTH_LONG).show();
                    }
                });
            } catch(NullPointerException e){
                pDialog.dismiss();
                //Internet problem warning
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(parentLayout, "Error: please check your internet connection", Snackbar.LENGTH_INDEFINITE)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //Will retry to get values once internet is checked
                                        new LoadAllResorts().execute();
                                    }
                                })
                                .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                                .show();
                        //Toast.makeText(Result.this ,"Error: please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] {TAG_NAME, TAG_PRICE, TAG_STATE, TAG_CITIES, TAG_DIST, TAG_ROOM_NAME, TAG_RATING}, new int[] { R.id.name, R.id.price, R.id.states, R.id.cities, R.id.distance_from_delhi,R.id.room_type, R.id.tob_rating});
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });
        }

    }
}






