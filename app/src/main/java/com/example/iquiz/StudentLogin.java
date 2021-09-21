package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentLogin extends AppCompatActivity implements View.OnClickListener {

    TextView txttitle;
    EditText usertext, passtext;
    Button submit;
    String username, password;
    String resultuser, resultpass;
    ImageView showpass;
    boolean isShowPassword = false;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");

    DatabaseReference dbref1 = db.child("Students");
    DatabaseReference dbref2 = db.child("Teachers");
    DatabaseReference dbref3 = db.child("Admin");

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

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

            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                Query dbquery = dbref1.orderByChild("username").equalTo(username);
                dbquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot values : dataSnapshot.getChildren()) {
                            StudentModel records = values.getValue(StudentModel.class);
                            //mylist.add(records);
                            //System.out.println("List Records are: "+mylist);
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
                                System.out.println("No Student has been Matched...");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
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

}