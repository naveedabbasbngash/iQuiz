package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class CalendarPage extends AppCompatActivity {

    DatePicker dp;
    Button btndone;
    String sdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        btndone = findViewById(R.id.done);
        dp = findViewById(R.id.datepick);

        dp.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                sdate = (monthOfYear+1)+"/"+dayOfMonth+"/"+year;
                System.out.println("sdate------------------------------------------------"+sdate);

            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("setdate",sdate);
                setResult(55,i);
                finish();
            }
        });
    }
}