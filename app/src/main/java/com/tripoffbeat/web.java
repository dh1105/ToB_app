package com.tripoffbeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Used to autofill form
 */

public class web extends AppCompatActivity {

    WebView webView;
    private final static String TAG = "web";
    String[] mail_list;
    String[] room_list;
    String[] room_price_list;
    String url = "http://139.59.34.30/quotation/";
    String btn_ci, btn_co, n, a, k, r, m, distance, activity, state, d, act_f, rn;
    String PageURL, PageTitle;
    //arbitrary value for the excersion field
    String dist = "Delhi", ex = "1";
    Intent in;
    ProgressDialog pDialog;
    ProgressDialog progressBar;
    View parentLayout;
    JSONparser jParser = new JSONparser();
    //Values being fetched from the DB
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DIST = "dist_del";
    private static final String TAG_ACT = "activity_name";
    private static final String TAG_STATE = "name";
    //URLs of php files used
    private static String get_details = "http://139.59.34.30/get_form.php";
    private static String get_activity_array = "http://139.59.34.30/get_activity_array.php";
    List<String> activity_list = new ArrayList<>();
    JSONArray act = null;
    String act_name [];
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        parentLayout = findViewById(android.R.id.content);
        webView = (WebView) findViewById(R.id.webview);
        in = getIntent();
        String act_i = in.getStringExtra("activity");
        Log.d("activity ", act_i);
        if(act_i.equals("mail")) {
            Bundle b = this.getIntent().getExtras();
            //All values received from the previous activities
            mail_list = b.getStringArray("mail_list");
            room_list = b.getStringArray("room_list");
            room_price_list = b.getStringArray("room_price_list");
            btn_ci = in.getStringExtra("checkin");
            btn_co = in.getStringExtra("checkout");
            n = in.getStringExtra("name");
            a = in.getStringExtra("adults");
            k = in.getStringExtra("kids");
            r = in.getStringExtra("rooms");
            m = in.getStringExtra("mail");
            d = in.getStringExtra("days");
            rn = in.getStringExtra("room_type");
            new GetDetails().execute();
            //JS used to fill the form in a webview
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    // TODO Auto-generated method stub
                    super.onPageStarted(view, url, favicon);

                    PageURL = view.getUrl();
                    actionBar = getSupportActionBar();

                    if (actionBar != null) {
                        actionBar.setSubtitle(PageURL);
                    }

                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    // TODO Auto-generated method stub
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    PageURL = view.getUrl();
                    PageTitle = view.getTitle();
                    actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setTitle(PageTitle);
                    }

                    actionBar.setSubtitle(PageURL);
                    //js is used to carry out JavaScript actions and filling details in the respective fields with strings
                    final String js = "javascript: " +
                            "var nameDoc = document.getElementsByName('name');" +
                            "nameDoc[0].value = '" + n + "';" +
                            "var checkOutDoc = document.getElementsByName('checkout');" +
                            "checkOutDoc[0].value = '" + btn_co + "';" +
                            "var noOFPaxDoc = document.getElementsByName('no_of_pax');" +
                            "noOFPaxDoc[0].value = '" + a + "';" + // a should be int based on ur HTML
                            "var noOFKidDoc = document.getElementsByName('no_of_kid');" +
                            "noOFKidDoc[0].value = '" + k + "';" + // a should be int based on ur HTML
                            "var noOFRoomsDoc = document.getElementsByName('no_of_rooms');" +
                            "noOFRoomsDoc[0].value = '" + r + "';" + // a should be int based on ur HTML
                            "var checkInDoc = document.getElementsByName('checkin');" +
                            "checkInDoc[0].value = '" + btn_ci + "';" +
                            "var email = document.getElementsByName('guest_email');" +
                            "email[0].value = '" + m + "';" +
                            "var resortName = document.getElementsByName('resort_name[]');" +
                            "resortName[0].value = '" + mail_list[0] + "';" +
                            "var distFrom = document.getElementsByName('distance_from[]');" +
                            "distFrom[0].value = '" + dist + "';" +
                            "var roomType = document.getElementsByName('room_category[]');" +
                            "roomType[0].value = '" + room_list[0] + "';" +
                            "var price = document.getElementsByName('package_price[]');" +
                            "price[0].value = '" + room_price_list[0] + "';" +
                            "var distance = document.getElementsByName('distance[]');" +
                            "distance[0].value = '" + distance + "';" +
                            "var ex = document.getElementsByName('excursions[]');" +
                            "ex[0].value = '" + ex + "';" +
                            "var act = document.getElementsByName('activities[]');" +
                            "act[0].value = '" + act_f + "';" +
                            "var dest = document.getElementsByName('destination');" +
                            "dest[0].value = '" + state + "';" +
                            "var days = document.getElementsByName('total_days');" +
                            "days[0].value = '" + d + "';" +
                            "javascript:(function(){" +
                            "l=document.getElementsByName('submit');" +
                            "e=document.createEvent('HTMLEvents');" +
                            "e.initEvent('click',true,true);" +
                            "l[0].dispatchEvent(e);" +
                            "})()";

                    if (Build.VERSION.SDK_INT >= 19) {
                        view.evaluateJavascript(js, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {

                            }
                        });
                    } else {
                        view.loadUrl(js);
                    }
                }
            });
        }
        else{
            webView.loadUrl(url);
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
        //pDialog1.dismiss();
        Log.i(TAG, "onDestroy()");
    }

    private class GetDetails extends AsyncTask<String, String, String> {

        /**
         * Class used to get details required
         * similar to all previous activities
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(web.this);
            pDialog.setMessage("Filling details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            Log.d("Mail List1: ", "arr: " + Arrays.toString(mail_list));
            try {
                JSONObject json = jParser.makeHttpRequest9(get_details, mail_list[0]);
                Log.d("Details : ", json.toString());
                int s = json.getInt(TAG_SUCCESS);

                if (s == 1) {
                    JSONArray b = json.getJSONArray(TAG_DIST);
                    JSONArray d = json.getJSONArray(TAG_STATE);
                    distance = b.getString(0);
                    state = d.getString(0);
                }
            } catch (Exception e) {
                pDialog.dismiss();
                e.printStackTrace();
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getApplicationContext(), OptionList.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark))
                            .show();
                }

            try{
                JSONObject json = jParser.makeHttpRequest9(get_activity_array, mail_list[0]);
                Log.d("Details : ", json.toString());
                int s = json.getInt(TAG_SUCCESS);

                if(s==1){
                    act = json.getJSONArray(TAG_ACT);
                    for(int i = 0; i < act.length(); i++){
                        activity = act.getString(i);
                        activity_list.add(activity);
                        Log.d("List: ", activity_list.toString());
                    }
                }
            } catch (Exception f){
                pDialog.dismiss();
                f.printStackTrace();
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), OptionList.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark))
                        .show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            act_name = new String[activity_list.size()];
            for(int i = 0; i<activity_list.size(); i++){
                act_name[i] = activity_list.get(i);
                Log.d("LIST: ", act_name[i]);
            }
            Log.d("LIST: ", "arr: " + Arrays.toString(mail_list));
            act_f = Arrays.toString(act_name);
            if(act_f.contains("[") || act_f.contains("]")){
                act_f = act_f.replace("[", " ");
                act_f = act_f.replace("]", " ");
            }
            Log.d("ACt: ", act_f);
            /*This for loop is extremely important, it replaces all singe quotes ( ' ) with (\') to make it possible for the JS
            * command to parse it correctly*/
            for(int i = 0; i < mail_list.length & i < room_list.length & i < room_price_list.length; i++){
                if(mail_list[i].contains("'")){
                    mail_list[i] = mail_list[i].replace("\'" , "\\'");
                    Log.d("mail list: ", mail_list[i]);
                }
                if(room_list[i].contains("'")){
                    room_list[i] = room_list[i].replace("\'", "\\'");
                    Log.d("room list: ", room_list[i]);
                }
                if(room_price_list[i].contains("'")){
                    room_price_list[i] = room_price_list[i].replace("\'", "\\'");
                    Log.d("room price list: ", room_price_list[i]);
                }
                if(mail_list[i].contains("?'")){
                    mail_list[i] = mail_list[i].replace("?" , " ");
                    Log.d("mail list: ", mail_list[i]);
                }
                if(room_list[i].contains("?")){
                    room_list[i] = room_list[i].replace("?", " ");
                    Log.d("room list: ", room_list[i]);
                }
                if(room_price_list[i].contains("?")){
                    room_price_list[i] = room_price_list[i].replace("", " ");
                    Log.d("room price list: ", room_price_list[i]);
                }
            }
            Log.d("List1: ","arr"+ Arrays.toString(mail_list));
            Log.d("List2: ","arr"+ Arrays.toString(room_list));
            Log.d("List3: ","arr"+ Arrays.toString(room_price_list));
        }
    }
}
