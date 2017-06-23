package com.tripoffbeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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

public class web extends AppCompatActivity {

    WebView webView;
    private final static String TAG = "web";
    String[] mail_list;
    String url = "http://139.59.34.30/quotation/";
    String btn_ci, btn_co, n, a, k, r, m, price, distance, activity, state, d, act_f;
    String dist = "Delhi", room_type = "2 Adults", ex = "1";
    Intent in;
    ProgressDialog pDialog;
    View parentLayout;
    JSONparser jParser = new JSONparser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DIST = "dist_del";
    private static final String TAG_ACT = "activity_name";
    private static final String TAG_STATE = "name";
    private static String get_details = "http://139.59.34.30/get_form.php";
    private static String get_activity_array = "http://139.59.34.30/get_activity_array.php";
    List<String> activity_list = new ArrayList<>();
    JSONArray act = null;
    String act_name [];

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
            mail_list = b.getStringArray("mail_list");
            btn_ci = in.getStringExtra("checkin");
            btn_co = in.getStringExtra("checkout");
            n = in.getStringExtra("name");
            a = in.getStringExtra("adults");
            k = in.getStringExtra("kids");
            r = in.getStringExtra("rooms");
            m = in.getStringExtra("mail");
            d = in.getStringExtra("days");
            new GetDetails().execute();
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setDisplayZoomControls(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
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
                            "roomType[0].value = '" + room_type + "';" +
                            "var price = document.getElementsByName('package_price[]');" +
                            "price[0].value = '" + price + "';" +
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
                    JSONArray a = json.getJSONArray(TAG_PRICE);
                    JSONArray b = json.getJSONArray(TAG_DIST);
                    JSONArray d = json.getJSONArray(TAG_STATE);
                    price = a.getString(0);
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
            Log.d("ACt: ", act_f);
        }
    }
}