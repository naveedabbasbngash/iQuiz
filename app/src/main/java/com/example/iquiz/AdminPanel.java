package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminPanel extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText txtname,txtuser,txtpass,txtcontact,txtreg,txtroll;
    LinearLayout puser,ppass,pcontact,proll,preg,psection,psemester,pdept,subpanel;
    TextView displaytext;

    Spinner typespin,secspin,semspin,deptspin;
    String[] types = {"STUDENT","TEACHER","SUBJECT"};
    String[] semesters = {"SEMESTER1","SEMESTER2","SEMESTER3","SEMESTER4","SEMESTER5","SEMESTER6","SEMESTER7","SEMESTER8"};
    String[] sections = {"A","B","C","D","E"};
    String[] departments  = {"CS","IT"};


    String utype,conttype,semstype,sectype,depttype;
    Button submit;

    Button aback;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        aback = findViewById(R.id.adback);
        aback.setOnClickListener(this);

        displaytext = findViewById(R.id.textinfo);

        txtuser = findViewById(R.id.user);
        txtpass = findViewById(R.id.pass);
        txtname = findViewById(R.id.name);
        txtcontact = findViewById(R.id.contact);
        txtreg = findViewById(R.id.reg);
        txtroll = findViewById(R.id.roll);

        puser = findViewById(R.id.userlayout);
        ppass = findViewById(R.id.passlayout);
        pcontact = findViewById(R.id.contactpanel);
        proll = findViewById(R.id.rollpanel);
        preg = findViewById(R.id.regpanel);
        psection = findViewById(R.id.sectionpanel);
        psemester = findViewById(R.id.semesterpanel);
        pdept = findViewById(R.id.deptpanel);

        submit = findViewById(R.id.register);
        submit.setOnClickListener(this);

        typespin = findViewById(R.id.type);
        secspin = findViewById(R.id.section);
        semspin = findViewById(R.id.semester);
        deptspin = findViewById(R.id.dept);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,types);
        typespin.setAdapter(adapter1);
        typespin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sections);
        secspin.setAdapter(adapter2);
        secspin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,semesters);
        semspin.setAdapter(adapter3);
        semspin.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,departments);
        deptspin.setAdapter(adapter4);
        deptspin.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            if (utype.equals("STUDENT")) {

                String uname = txtname.getText().toString();
                String uuser = txtuser.getText().toString();
                String upass = txtpass.getText().toString();
                String stdreg = txtreg.getText().toString();
                String stdroll = txtroll.getText().toString();

                //int ureg = Integer.parseInt(txtreg.getText().toString());
                //int uroll = Integer.parseInt(txtroll.getText().toString());
                String usec = sectype;
                String usem = semstype;
                String udept = depttype;

                //String strreg = String.valueOf(ureg);
                //String strrol = String.valueOf(ureg);

                if(!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(uuser) && !TextUtils.isEmpty(upass) && !TextUtils.isEmpty(stdreg) && !TextUtils.isEmpty(stdroll) )
                {
                    if(upass.length()>7)
                    {
                        int ureg = Integer.parseInt(txtreg.getText().toString());
                        int uroll = Integer.parseInt(txtroll.getText().toString());
                        StudentModel rec = new StudentModel(uname, uuser, upass, usec, usem, udept, uroll, ureg);

                        databaseReference= FirebaseDatabase.getInstance().getReference("INU").child("Students");
                        String mykey = databaseReference.push().getKey();
                        databaseReference.child(mykey).setValue(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Record has been Added...");
                                Toast.makeText(AdminPanel.this, "Student Account has been Created", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtname.setText("");
                        txtuser.setText("");
                        txtpass.setText("");
                        txtreg.setText("");
                        txtroll.setText("");
                    }
                    else
                    {
                        Toast.makeText(this, "Min Password Length is: 8", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Text Field is Empty!", Toast.LENGTH_SHORT).show();
                }
                //StdRecord rec = new StdRecord();
                //StdRecord rec = new StdRecord(String name, String username, String password, String section, String semester, String department, int roll, int reg)


            } else if (utype.equals("TEACHER")) {

                String uname = txtname.getText().toString();
                String uuser = txtuser.getText().toString();
                String upass = txtpass.getText().toString();
                String ucontact = txtcontact.getText().toString();

                if(!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(uuser) && !TextUtils.isEmpty(upass) && !TextUtils.isEmpty(ucontact))
                {
                    if(upass.length()>7)
                    {
                        TeacherModel rec = new TeacherModel(uname, uuser, upass, ucontact);

                        databaseReference= FirebaseDatabase.getInstance().getReference("INU").child("Teachers");
                        String mykey = databaseReference.push().getKey();
                        databaseReference.child(mykey).setValue(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                System.out.println("Record has been Added...");
                                Toast.makeText(AdminPanel.this, "Teacher Account has been Created", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtname.setText("");
                        txtuser.setText("");
                        txtpass.setText("");
                        txtcontact.setText("");
                    }
                    else
                    {
                        Toast.makeText(this, "Min Password Length is: 8", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Text Field is Empty!", Toast.LENGTH_SHORT).show();
                }


            }
            else if (utype.equals("SUBJECT")) {

                String uname = txtname.getText().toString();

                if(!TextUtils.isEmpty(uname))
                {
                    SubjectModel rec = new SubjectModel(uname);

                    databaseReference= FirebaseDatabase.getInstance().getReference("INU").child("Subjects");
                    String mykey = databaseReference.push().getKey();
                    databaseReference.child(mykey).setValue(rec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Record has been Added...");
                            Toast.makeText(AdminPanel.this, "Subject has been Created", Toast.LENGTH_SHORT).show();
                        }
                    });
                    txtname.setText("");
                }
                else
                {
                    Toast.makeText(this, "Text Field is Empty!", Toast.LENGTH_SHORT).show();
                }


            }
        }
        else if (v.getId() == R.id.adback) {
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //System.out.println("SElected type is: "+utype+"==================");

        if(parent.getId()==R.id.type) {
            utype = parent.getItemAtPosition(position).toString();
            if (utype.equals("STUDENT")) {
                displaytext.setText("STUDENT REGISTRATION FORM");
                pcontact.setVisibility(LinearLayout.INVISIBLE);
                pcontact.setVisibility(LinearLayout.GONE);

                proll.setVisibility(LinearLayout.VISIBLE);
                preg.setVisibility(LinearLayout.VISIBLE);
                psection.setVisibility(LinearLayout.VISIBLE);
                psemester.setVisibility(LinearLayout.VISIBLE);
                pdept.setVisibility(LinearLayout.VISIBLE);
            } else if (utype.equals("TEACHER")) {
                displaytext.setText("TEACHER REGISTRATION FORM");
                pcontact.setVisibility(LinearLayout.VISIBLE);
                proll.setVisibility(LinearLayout.INVISIBLE);
                proll.setVisibility(LinearLayout.GONE);

                preg.setVisibility(LinearLayout.INVISIBLE);
                preg.setVisibility(LinearLayout.GONE);

                psection.setVisibility(LinearLayout.INVISIBLE);
                psection.setVisibility(LinearLayout.GONE);

                psemester.setVisibility(LinearLayout.INVISIBLE);
                psemester.setVisibility(LinearLayout.GONE);

                pdept.setVisibility(LinearLayout.INVISIBLE);
                pdept.setVisibility(LinearLayout.GONE);
            }
            else if (utype.equals("SUBJECT")) {
                displaytext.setText("SUBJECT REGISTRATION FORM");

                puser.setVisibility(LinearLayout.INVISIBLE);
                puser.setVisibility(LinearLayout.GONE);

                ppass.setVisibility(LinearLayout.INVISIBLE);
                ppass.setVisibility(LinearLayout.GONE);

                proll.setVisibility(LinearLayout.INVISIBLE);
                proll.setVisibility(LinearLayout.GONE);

                preg.setVisibility(LinearLayout.INVISIBLE);
                preg.setVisibility(LinearLayout.GONE);

                psection.setVisibility(LinearLayout.INVISIBLE);
                psection.setVisibility(LinearLayout.GONE);

                psemester.setVisibility(LinearLayout.INVISIBLE);
                psemester.setVisibility(LinearLayout.GONE);

                pdept.setVisibility(LinearLayout.INVISIBLE);
                pdept.setVisibility(LinearLayout.GONE);

                pcontact.setVisibility(LinearLayout.INVISIBLE);
                pcontact.setVisibility(LinearLayout.GONE);
            }
        }
        else if(parent.getId()==R.id.contact)
        {
            conttype = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId()==R.id.semester)
        {
            semstype = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId()==R.id.section)
        {
            sectype = parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId()==R.id.dept)
        {
            depttype = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}