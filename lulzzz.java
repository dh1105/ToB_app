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
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            /*states = getIntent().getExtras().getString("states");
            min_budget = getIntent().getExtras().getString("min_budget");
            max_budget = getIntent().getExtras().getString("max_budget");
            activity = getIntent().getExtras().getString("activity");*/

            params.add(new BasicNameValuePair("states", states));
            params.add(new BasicNameValuePair("min_budget", min_budget));
            params.add(new BasicNameValuePair("max_budget", max_budget));
            params.add(new BasicNameValuePair("activity", activity));
            Log.d("Passing parameters: ", params.toString());


            // Check your log cat for JSON response
            try {
                JSONObject json = jParser.makeHttpRequest(url_all_resorts, "POST", params);
                Log.d("All Resorts : ", json.toString());
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // resorts found
                    // Getting Array of resorts
                    resorts = json.getJSONArray(TAG_RESORT);
                    rates = json.getJSONArray(TAG_PRICE);

                    int i;

                    // looping through All resorts
                    for (i = 0; i < resorts.length() & i < rates.length(); i++) {
                        //JSONObject c = resorts.getJSONObject(i);
                        //JSONObject d = rates.getJSONObject(i);

                        // Storing each json item in variable
                        String name = resorts.getString(i);
                        String price = rates.getString(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, name);
                        map.put(TAG_ROOM_PRICE, price);

                        // adding HashList to ArrayList
                        resortsList.add(map);
                        Log.d("Details: ", resortsList.toString());
                    }

                } else {
                    // no resorts found
                    pDialog.dismiss();
                    try {
                        t.sleep(500);
                    } catch (Exception f) {
                    }
                    Snackbar.make(parentLayout, "No resorts found", Snackbar.LENGTH_INDEFINITE)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(getApplicationContext(), OptionList.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                pDialog.dismiss();
                try {
                    t.sleep(500);
                } catch (Exception f) {
                }
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), OptionList.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark ))
                        .show();
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
                    ListAdapter adapter = new SimpleAdapter(Result.this, resortsList, R.layout.activity_result, new String[] { TAG_NAME, TAG_ROOM_PRICE}, new int[] { R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading = new ProgressDialog(Result.this);
            //this method will be running on UI thread
            pdLoading.setMessage("Loading resorts. Please wait...");
            pdLoading.setIndeterminate(false);
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://139.59.34.30/get_resort_details_Copy1.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
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

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
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

                    // Pass data to onPostExecute method
                    String a = result.toString();
                    Log.d("Resorts: ", a);
                    return a;

                } else {

                    return "unsuccessful";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String a) {

            //this method will be running on UI thread

            pdLoading.dismiss();

            if (a.equals("exception") || a.equals("unsuccessful")) {
                try {
                    t.sleep(500);
                } catch (Exception f) {
                }
                Snackbar.make(parentLayout, "Error connecting to the server", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplicationContext(), OptionList.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_blue_dark))
                        .show();
            } else {
                try {
                    JSONArray jArray = new JSONArray(a);
                    for(int i = 0; i < jArray.length(); i++){
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        list.add(jsonObject.getString(TAG_NAME));
                        list2.add(jsonObject.getString(TAG_ROOM_PRICE));
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
                listNames.addAll(list);
                adapter1.notifyDataSetChanged();
                listPrices.addAll(list2);
                adapter2.notifyDataSetChanged();
            }
        }