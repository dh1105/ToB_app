package com.tripoffbeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by user on 6/28/2017.
 */

public class Popup extends Activity implements View.OnClickListener {

    Button yes, no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(this);

        no = (Button) findViewById(R.id.no);
        no.setOnClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.95), (int)(height*.3));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.yes:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_LONG).show();
                startActivity(i);
                break;

            case R.id.no:
                finish();
                break;

        }
    }

}
