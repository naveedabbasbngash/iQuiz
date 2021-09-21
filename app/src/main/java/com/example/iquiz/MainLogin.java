package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainLogin extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView txttitle,logintxt;
    EditText usertext, passtext;
    Button submit;
    String username, password;
    String resultuser, resultpass;
    ImageView showpass;
    boolean isShowPassword = false;
    Spinner spinType;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");

    DatabaseReference dbref1 = db.child("Students");
    DatabaseReference dbref2 = db.child("Teachers");
    DatabaseReference dbref3 = db.child("Admin");

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        spinType = findViewById(R.id.usertype);
        spinType.setOnItemSelectedListener(this);

        logintxt = findViewById(R.id.logintext);

        usertext = findViewById(R.id.user);
        passtext = findViewById(R.id.pass);
        showpass = findViewById(R.id.eyecheck);
        showpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowPassword) {
                    showpass.setBackgroundResource(0);
                    passtext.setTransformationMethod(new PasswordTransformationMethod());
                    showpass.setImageDrawable(getResources().getDrawable(R.drawable.eyeoff));
                    isShowPassword = false;
                } else {
                    showpass.setBackgroundResource(0);
                    passtext.setTransformationMethod(null);
                    showpass.setImageDrawable(getResources().getDrawable(R.drawable.eyeon));
                    isShowPassword = true;
                }
            }
        });

        submit = findViewById(R.id.login);
        submit.setOnClickListener(this);

        txttitle = findViewById(R.id.logintitle);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            username = usertext.getText().toString();
            password = passtext.getText().toString();

            String userType = spinType.getSelectedItem().toString();

            if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password))
            {
                if(userType.equals("Teacher"))
                {
                    Query dbquery = dbref2.orderByChild("username").equalTo(username);
                    dbquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for (DataSnapshot values : dataSnapshot.getChildren()) {
                                    TeacherModel records = values.getValue(TeacherModel.class);

                                    if (records.getPassword().equals(password)) {
                                        System.out.println("Teacher Matched...");
                                        System.out.println("Teacher ID: " + values.getKey());
                                        resultuser = records.getName();
                                        resultpass = records.getPassword();
                                        loginTeacher(resultuser,resultpass);
                                    } else {
                                        Toast.makeText(MainLogin.this, "Teacher Matched but Invalid Password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(MainLogin.this, "No Teacher has been Matched", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else if(userType.equals("Student"))
                {
                    Query dbquery = dbref1.orderByChild("username").equalTo(username);
                    dbquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())
                            {
                                for (DataSnapshot values : dataSnapshot.getChildren()) {
                                    StudentModel records = values.getValue(StudentModel.class);

                                    if (records.getPassword().equals(password)) {
                                        System.out.println("Student Matched...");
                                        System.out.println("Student ID: " + values.getKey());
                                        resultuser = records.getName();
                                        resultpass = records.getPassword();
                                        int roll = records.getRoll();
                                        System.out.println("Roll Number of The Current Student: " + roll + "---*****---");
                                        String section = records.getSection();
                                        String dept = records.getDepartment();
                                        String semester = records.getSemester();

                                        loginStudent(resultuser, resultpass, section, dept, semester, roll);
                                    } else {
                                        Toast.makeText(MainLogin.this, "Student found but Invalid password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(MainLogin.this, "No Student has been Matched", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(userType.equals("Admin"))
                {
                    if(username.equals("ADMIN") && password.equals("INU123@@"))
                    {
                        usertext.setText("");
                        passtext.setText("");
                        Intent i = new Intent(this,AdminPanel.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(MainLogin.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loginTeacher(String techname,String techpass)
    {
        sharedPreferences = getSharedPreferences("myappdata", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ttoken",techname);
        editor.commit();

        System.out.println("Student Username is: --------------------------------------------------"+username);

        //Intent i = new Intent(this,TeacherPanel.class);
        Intent i = new Intent(this,TeacherPanel.class);
        i.putExtra("teachername",techname);
        i.putExtra("username",username);
        i.putExtra("teacherpass",techpass);

        usertext.setText("");
        passtext.setText("");

        startActivity(i);
    }

    public void loginStudent (String stdname, String stdpass, String stdsection, String
            stddept, String stdsemester,int stdroll)
    {
        sharedPreferences = getSharedPreferences("myappdata", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("stoken", stdname);
        editor.putString("ssection", stdsection);
        editor.putString("sdept", stddept);
        editor.putString("ssem", stdsemester);
        editor.putInt("sroll", stdroll);
        editor.commit();

        System.out.println("Student Username is: --------------------------------------------------" + username);

        Intent i = new Intent(this, StudentPanel.class);
        i.putExtra("studentname", stdname);
        i.putExtra("username", username);
        i.putExtra("studentpass", stdpass);

        usertext.setText("");
        passtext.setText("");

        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String user = spinType.getItemAtPosition(i).toString();
        if(user.equals("Admin"))
        {
            logintxt.setText("I-Quiz Admin Login Form");
        }
        else if(user.equals("Teacher"))
        {
            logintxt.setText("I-Quiz Teacher Login Form");
        }
        else if(user.equals("Student"))
        {
            logintxt.setText("I-Quiz Student Login Form");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}