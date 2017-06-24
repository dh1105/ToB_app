package com.tripoffbeat;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OptionList extends AppCompatActivity implements View.OnClickListener{

    Spinner states, min_budget, max_budget, activity, dist;
    Button searchHotel, previous, clear;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listActivity = new ArrayList<>();
    ArrayAdapter<String> adapter1, adapter2;
    private static final String TAG = "OptionList";
    Intent i;
    CheckBox states_box, budget_box, activity_box;
    String empty = "Empty";
    RadioGroup r;
    RadioButton dist_delkm;
    RadioButton time_del;
    List<String> t = null, km = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_list);
        searchHotel = (Button) findViewById(R.id.searchHotel);
        searchHotel.setOnClickListener(this);
        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(this);
        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(this);
        i = getIntent();

        r = (RadioGroup) findViewById(R.id.r);
        dist_delkm = (RadioButton) findViewById(R.id.dist_delkm);
        time_del = (RadioButton) findViewById(R.id.time_del);

        states_box = (CheckBox) findViewById(R.id.states_box);
        budget_box = (CheckBox) findViewById(R.id.budget_box);
        activity_box = (CheckBox) findViewById(R.id.activity_box);

        states = (Spinner) findViewById(R.id.states);
        states.setVisibility(View.INVISIBLE);
        adapter1 = new ArrayAdapter<String>(this, R.layout.spinner, R.id.states, listItems);

        states_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    states.setVisibility(View.VISIBLE);
                    states.setAdapter(adapter1);
                    states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                            Toast.makeText(OptionList.this, parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT).show();
                        }

                        public void onNothingSelected(AdapterView<?> parent){

                        }
                    });
                } else {
                    states.setVisibility(View.INVISIBLE);
                }
            }
        });
        min_budget = (Spinner) findViewById(R.id.min_budget);
        min_budget.setVisibility(View.INVISIBLE);
        max_budget = (Spinner) findViewById(R.id.max_budget);
        max_budget.setVisibility(View.INVISIBLE);
        budget_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    min_budget.setVisibility(View.VISIBLE);
                    max_budget.setVisibility(View.VISIBLE);
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
                    min_budget.setVisibility(View.INVISIBLE);
                    max_budget.setVisibility(View.INVISIBLE);
                }
            }
        });
        activity = (Spinner) findViewById(R.id.activity);
        activity.setVisibility(View.INVISIBLE);
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner, R.id.activity, listActivity);
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
                    activity.setVisibility(View.INVISIBLE);
                    //listActivity.clear();
                }
            }
        });

        dist = (Spinner) findViewById(R.id.dist_del);
        dist.setVisibility(View.INVISIBLE);

        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.dist_delkm:
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

        Log.i(TAG, "onCreate()");

    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.searchHotel:
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

            case R.id.previous:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://139.59.34.30/quotation/"));
                listActivity.clear();
                listItems.clear();
                startActivity(i);
                break;

            case R.id.clear:
                if(dist_delkm.isChecked() || time_del.isChecked()){
                    r.clearCheck();
                    dist.setVisibility(View.INVISIBLE);
                    Toast.makeText(OptionList.this, "Selection cleared", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(OptionList.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private class BackTask extends AsyncTask<Void,Void,Void> {
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
                HttpPost httppost= new HttpPost("http://139.59.34.30/get_states.php");
                HttpResponse response=httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                // Get our response as a String.
                HttpClient httpclient1 = new DefaultHttpClient();
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
                    list2.add(jsonObject.getString("activity_name"));
                }
            }
            catch(JSONException e){
                    e.printStackTrace();
                }
            return null;
        }
        protected void onPostExecute(Void result){
            listItems.addAll(list);
            adapter1.notifyDataSetChanged();
            listActivity.addAll(list2);
            adapter2.notifyDataSetChanged();
        }
    }
}

