package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentChangePassword extends AppCompatActivity implements View.OnClickListener {

    EditText txtold,txtnew;
    Button btnChange,btnBack;
    String oldpass,newpass;

    SharedPreferences sharedPreferences;
    String stdname;
    String ukey;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_change_password);

        txtold = findViewById(R.id.oldpass);
        txtnew = findViewById(R.id.newpass);

        btnChange = findViewById(R.id.changepass);
        btnChange.setOnClickListener(this);

        btnBack = findViewById(R.id.back);
        btnBack.setOnClickListener(this);

        //sharedPreferences = getSharedPreferences("myappdata", this.MODE_PRIVATE);
        //stdname = sharedPreferences.getString("stoken",null);
        Intent i = getIntent();
        stdname = i.getStringExtra("username");
        System.out.println("-------------------------------STD NAME: "+stdname);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.changepass)
        {
            oldpass = txtold.getText().toString();
            newpass = txtnew.getText().toString();
            if(!TextUtils.isEmpty(oldpass) && !TextUtils.isEmpty(newpass))
            {
                Query dbquery = dbref1.orderByChild("username").equalTo(stdname);
                dbquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot values : dataSnapshot.getChildren()) {
                            StudentModel records = values.getValue(StudentModel.class);

                            if (records.getPassword().equals(oldpass)) {
                                ukey = values.getKey();
                                changeit(ukey);
                                System.out.println("==========================================="+ukey+"===========================================================");

                            } else {
                                Toast.makeText(StudentChangePassword.this, "Your Old Password has not been Matched...", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            else
            {
                Toast.makeText(this, "Fields are Empty", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId()==R.id.back)
        {
            finish();
        }

    }

    public void changeit(String key)
    {
        dbref1.child(key).child("password").setValue(newpass);
        Toast.makeText(StudentChangePassword.this, "Password Has been Changed", Toast.LENGTH_SHORT).show();
        txtold.setText("");
        txtnew.setText("");
    }
}