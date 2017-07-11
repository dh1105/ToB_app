package com.tripoffbeat;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by user on 7/11/2017.
 */

public class Logout {

    private Sessions sessions;

    private Context mContext;

    public Logout(Context mContext){
        this.mContext = mContext;
    }

    public void showLogout(){
        sessions =  new Sessions(mContext);

        AlertDialog.Builder log = new AlertDialog.Builder(mContext);

        log.setTitle("Logout");

        log.setMessage("Are you sure you want to log out?");

        log.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sessions.logoutUser();
            }
        });

        log.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        log.setCancelable(true);

        log.show();
    }

}
