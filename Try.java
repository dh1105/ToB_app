
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


            params.add(new BasicNameValuePair("states", states));
            params.add(new BasicNameValuePair("min_budget", min_budget));
            params.add(new BasicNameValuePair("max_budget", max_budget));
            params.add(new BasicNameValuePair("activity", activity));


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
                        JSONObject c = resorts.getJSONObject(i);
                        JSONObject d = rates.getJSONObject(i);

                        // Storing each json item in variable
                        String name = c.getString(TAG_NAME);
                        String price = d.getString(TAG_ROOM_PRICE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, name);
                        map.put(TAG_ROOM_PRICE, price);

                        // adding HashList to ArrayList
                        resortsList.add(map);
                    }
                    /*for (int i = 0; i < rates.length(); i++) {
                        JSONObject d = rates.getJSONObject(i);

                        // Storing each json item in variable
                        String price = d.getString(TAG_ROOM_PRICE);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ROOM_PRICE, price);

                        // adding HashList to ArrayList
                        resortsList.add(map);
                    }*/
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
                    ListAdapter adapter = new SimpleAdapter(Result.this, resortsList, R.layout.all_resorts, new String[] { TAG_ROOM_PRICE, TAG_NAME}, new int[] { R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }