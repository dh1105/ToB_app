package com.tripoffbeat;

import android.net.Uri;
import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 5/24/2017.
 * Class used to make a connection to the DB and get data in the form of a JSON string and parse it in order to set values
 * in the respective fields. The Result, ResortDetails and web classes use this class for DB communication. Different methods
 * present to accomodate all cases of parameters passed through the filters from OptionList.class to Result.class
 */

public class JSONparser {
    static InputStream is;
    static JSONObject jObj;
    static String json = "";
    URL u = null;
    HttpURLConnection conn;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    // constructor
    public JSONparser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String states, String min_budget, String max_budget, String activity) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();
            Log.d("Query: ", query);

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest1(String url, String res_name) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("resort_name", res_name);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }


    public JSONObject makeHttpRequest3(String url, String activity) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("activity", activity);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest4(String url, String min_budget, String max_budget) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest5(String url, String states) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest6(String url, String states, String activity) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("activity", activity);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest7(String url, String min_budget, String max_budget, String activity) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest8(String url, String states, String min_budget, String max_budget) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest9(String url, String resort) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("resort_name", resort);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest15(String url_all_resorts, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest16(String url_all_resorts, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest10(String url_all_resorts, String states, String time_del) {
        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest11(String url_all_resorts, String states, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest12(String url_all_resorts, String min_budget, String max_budget, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest13(String url_all_resorts, String min_budget, String max_budget, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest14(String url_all_resorts, String activity, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("activity", activity)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest17(String url_all_resorts, String states, String min_budget, String max_budget, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget).appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest18(String url_all_resorts, String states, String min_budget, String max_budget, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget).appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest19(String url_all_resorts, String min_budget, String max_budget, String activity, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest20(String url_all_resorts, String min_budget, String max_budget, String activity, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest21(String url_all_resorts, String states, String activity, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest22(String url_all_resorts, String states, String activity, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest23(String url_all_resorts, String states, String min_budget, String max_budget, String activity, String dist_delkm) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("dist_del", dist_delkm);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest24(String url_all_resorts, String states, String min_budget, String max_budget, String activity, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("states", states)
                    .appendQueryParameter("min_budget", min_budget)
                    .appendQueryParameter("max_budget", max_budget)
                    .appendQueryParameter("activity", activity)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest25(String url_all_resorts, String activity, String time_del) {

        try {
            // Enter URL address where your php file resides
            u = new URL(url_all_resorts);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("activity", activity)
                    .appendQueryParameter("time_del", time_del);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public JSONObject makeHttpRequest26(String get_user, String email, String pass) {

        try {
            // Enter URL address where your php file resides
            u = new URL(get_user);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Making HTTP request
        try {
            // check for request method
            conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", email).appendQueryParameter("pass", pass);
            Log.d("Format: ", builder.toString());
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                json = result.toString();
                Log.d("Data received: ", json);
            }

        } catch (IOException e) {
            Log.e("Buffer Error: ", "Error converting result " + e.toString());
        }finally {
            conn.disconnect();
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
            Log.d("jObj: ", jObj.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser: ", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }
}

