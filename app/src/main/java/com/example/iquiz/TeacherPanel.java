package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherPanel extends AppCompatActivity implements View.OnClickListener {

    Button tback,tresult,tquestion,upload,downloadlist,changepassword;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel);

        tback = findViewById(R.id.teachback);
        tback.setOnClickListener(this);
        upload= findViewById(R.id.uploadassign);
        upload.setOnClickListener(this);

        tresult = findViewById(R.id.viewresult);
        tresult.setOnClickListener(this);

        tquestion = findViewById(R.id.addquestion);
        tquestion.setOnClickListener(this);

        downloadlist = findViewById(R.id.studentsassign);
        downloadlist.setOnClickListener(this);

        changepassword = findViewById(R.id.changepass);
        changepassword.setOnClickListener(this);

        Intent i = getIntent();
        username = i.getStringExtra("username");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.viewresult)
        {
            Intent i = new Intent(this,TeacherResultPanel.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.addquestion)
        {
            //Intent i = new Intent(this,Dashboad.class);
            Intent i = new Intent(this,TeacherCreateQuiz.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.uploadassign)
        {
            Intent i = new Intent(this, TeacherUploadAssignment.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.studentsassign)
        {
            Intent i = new Intent(this, TeacherDownloadAssignment.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.changepass)
        {
            Intent i = new Intent(this,TeacherChangePassword.class);
            i.putExtra("username",username);
            startActivity(i);
        }
        else if(v.getId()==R.id.teachback)
        {
            finish();
        }
    }
}