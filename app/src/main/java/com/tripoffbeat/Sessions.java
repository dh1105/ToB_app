package com.tripoffbeat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by user on 6/30/2017.
 */

public class Sessions {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ToB";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "Email";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "Pass";

    // Constructor
    public Sessions(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String password, Boolean a){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, a);

        // Storing name in pref
        editor.putString(KEY_NAME, email);

        // Storing email in pref
        editor.putString(KEY_EMAIL, password);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Toast.makeText(_context, "Please login to continue", Toast.LENGTH_LONG).show();

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Check login method wil check user login status
     * if it is true it will direct you to the OptionList.class
     * */

    public void login(){
        if(this.isLoggedIn()){
            Intent i = new Intent(_context, OptionList.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    //Returns true if you are logged in
    public boolean getVal(){
        return pref.getBoolean(IS_LOGIN, isLoggedIn());
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
