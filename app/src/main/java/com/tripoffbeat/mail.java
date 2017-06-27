package com.tripoffbeat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class mail extends AppCompatActivity implements View.OnClickListener {

    Button email;
    String[] mail_lists;
    String[] room_list;
    String[] room_price_list;
    String rn;
    EditText btn_checkin, btn_checkout, name, adults, kids, rooms, mail, days;
    private static final String TAG = "mail";
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog.OnDateSetListener date1;
    String btn_ci, btn_co, n, a, k, r, m, d;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        mail = (EditText) findViewById(R.id.client_email);
        btn_checkin = (EditText) findViewById(R.id.btn_checkin);
        btn_checkout = (EditText) findViewById(R.id.btn_checkout);
        name = (EditText) findViewById(R.id.name);
        adults = (EditText) findViewById(R.id.adults);
        kids = (EditText) findViewById(R.id.kids);
        rooms = (EditText) findViewById(R.id.rooms);
        days = (EditText) findViewById(R.id.days);

        btn_checkin.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel1();
            }

        };
        email = (Button)findViewById(R.id.mail_button);
        email.setOnClickListener(this);
        Bundle b = this.getIntent().getExtras();
        mail_lists = b.getStringArray("mail_list");
        room_list = b.getStringArray("room_list");
        room_price_list = b.getStringArray("room_price_list");
        i = getIntent();
        Log.d("Mail List: ", "arr: " + Arrays.toString(mail_lists));
        Log.d("Room name List: ", "arr: " + Arrays.toString(room_list));
        Log.d("Room price List: ", "arr: " + Arrays.toString(room_price_list));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mail_button:
                btn_ci = btn_checkin.getText().toString();
                btn_co = btn_checkout.getText().toString();
                n = name.getText().toString();
                a = adults.getText().toString();
                k = kids.getText().toString();
                r = rooms.getText().toString();
                m = mail.getText().toString();
                d = days.getText().toString();
                if(btn_co.equals("") || btn_ci.equals("") || n.equals("") || a.equals("") || k.equals("") || r.equals("") || m.equals("") || d.equals("")){
                    Toast.makeText(mail.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Bundle b = new Bundle();
                    b.putStringArray("mail_list", mail_lists);
                    b.putStringArray("room_list", room_list);
                    b.putStringArray("room_price_list", room_price_list);
                    Intent i = new Intent(mail.this, web.class);
                    i.putExtra("checkin", btn_ci);
                    i.putExtra("checkout", btn_co);
                    i.putExtra("name", n);
                    i.putExtra("adults", a);
                    i.putExtra("kids", k);
                    i.putExtra("rooms", r);
                    i.putExtra("mail", m);
                    i.putExtra("days", d);
                    i.putExtra("activity", TAG);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
                break;

            case R.id.btn_checkin:
                new DatePickerDialog(mail.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.btn_checkout:
                new DatePickerDialog(mail.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            default:
                break;
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
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        btn_checkin.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        btn_checkout.setText(sdf.format(myCalendar.getTime()));
        getDiffDays();
    }

    private void getDiffDays(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        try {
            Date date1 = simpleDateFormat.parse(btn_checkin.getText().toString());
            Date date2 = simpleDateFormat.parse(btn_checkout.getText().toString());

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            cal1.setTime(date1);
            cal2.setTime(date2);


            long diff = cal2.getTimeInMillis() - cal1.getTimeInMillis();
            float dayCount = (float) diff / (24 * 60 * 60 * 1000);
            int f = (int) dayCount;
            if(f <= 0){
                Toast.makeText(mail.this, "Please select a valid date", Toast.LENGTH_SHORT).show();
                btn_checkout.getText().clear();
            }
            else {
                days.setText(Integer.toString(f));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

