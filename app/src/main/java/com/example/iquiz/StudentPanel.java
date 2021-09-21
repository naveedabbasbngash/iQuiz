package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentPanel extends AppCompatActivity implements View.OnClickListener {

    Button sback,qpanel,uploadassign,downloadassign, quizresult, changepassword;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);

        sback = findViewById(R.id.stdback);
        sback.setOnClickListener(this);

        qpanel = findViewById(R.id.quiz);
        qpanel.setOnClickListener(this);

        uploadassign = findViewById(R.id.uploadassign);
        uploadassign.setOnClickListener(this);

        downloadassign = findViewById(R.id.downloadassign);
        downloadassign.setOnClickListener(this);

        changepassword = findViewById(R.id.changepass);
        changepassword.setOnClickListener(this);

        quizresult = findViewById(R.id.stdresult);
        quizresult.setOnClickListener(this);

        Intent i = getIntent();
        username = i.getStringExtra("username");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.quiz)
        {
            Intent i = new Intent(this,StudentQuizStarter.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.uploadassign)
        {
            Intent i = new Intent(this,StudentUploadAssignment.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.downloadassign)
        {
            Intent i = new Intent(this,StudentDownloadAssignment.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.changepass)
        {
            Intent i = new Intent(this,StudentChangePassword.class);
            i.putExtra("username",username);
            startActivity(i);
        }
        else if(v.getId()==R.id.stdresult)
        {
            Intent i = new Intent(this,StudentResultStarter.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.stdback)
        {
            finish();
        }
    }
}