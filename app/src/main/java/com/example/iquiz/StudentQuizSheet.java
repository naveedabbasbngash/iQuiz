package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StudentQuizSheet extends AppCompatActivity {

    DatabaseReference retrieving,retrievingcount;
    DatabaseReference databaseReference;

    //FirebaseAuth firebaseAuth;
    String mobile_number;
    EditText edit_student_id;
    TextView txt_quize_mcqs_number,result,txt_mcqs_txt,mTextField_Hours,mTextField_Minutes,mTextField_Seconds;
    RadioButton id_a,id_b,id_c,id_d;
    Button btn_next,btn_confirm,btn_save_marks;
    Double sum,old_marks,new_marks;
    int count=1;
    Double finnal_marks;
    Double test_marks;
    long counter;
    int totTime;

    String semester,section,department,rollnum;

    SharedPreferences sharedPreferences;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");

    DatabaseReference dbref1 = db.child("Students");
    DatabaseReference dbref2 = db.child("Teachers");
    DatabaseReference dbref3  = db.child("Quizes");
    DatabaseReference dbref4  = db.child("Marks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_sheet);

        Intent i = getIntent();
        totTime = i.getIntExtra("totmins",0);


        databaseReference = FirebaseDatabase.getInstance().getReference("INU");

        sharedPreferences = getSharedPreferences("myappdata",this.MODE_PRIVATE);
        String username = sharedPreferences.getString("stoken","INVALID");
        semester = sharedPreferences.getString("ssem","INVALID");
        department = sharedPreferences.getString("sdept","INVALID");
        section = sharedPreferences.getString("ssection","INVALID");
        int roll = sharedPreferences.getInt("sroll",5555);

        rollnum = String.valueOf(roll);
        System.out.println("STudent Roll Number is: "+roll+" - - - - - - "+rollnum);


        Initalization_Widgets();

        //Count Down Timer Start
        //new CountDownTimer(900000, 1000) {
        int TotalMins = (totTime*60)*1000;

        new CountDownTimer(TotalMins, 1000) {
            int counter = 60;
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;

                if(hours>0)
                {
                    mTextField_Hours.setText(String.valueOf(hours));
                }
                else
                {
                    mTextField_Hours.setText(String.valueOf(0));
                }
                mTextField_Minutes.setText(String.valueOf(minutes));
                if(counter<=0)
                {
                    mTextField_Seconds.setText(String.valueOf(0));
                    counter = 60;
                }
                else
                {
                    counter--;
                    mTextField_Seconds.setText(String.valueOf(counter));
                }
                //mTextField.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                //mTextField_Hours.setText("Finish!");
                mTextField_Hours.setText("00");
                mTextField_Minutes.setText("00");
                mTextField_Seconds.setText("00");
                //InsertMarks();
                finish();
            }
        }.start();
        retrevingdata();
    }

    public void Initalization_Widgets() {

        mTextField_Hours = findViewById(R.id.txt_hours);
        mTextField_Minutes = findViewById(R.id.txt_minutes);
        mTextField_Seconds = findViewById(R.id.txt_seconds);
        //edit_student_id=(EditText) findViewById(R.id.edit_student_id);
        txt_quize_mcqs_number=(TextView) findViewById(R.id.txt_quize_mcqs_number);
        result=(TextView)findViewById(R.id.result);
        txt_mcqs_txt=(TextView)findViewById(R.id.txt_mcqs_txt);
        id_a=(RadioButton)findViewById(R.id.id_a);
        id_b=(RadioButton)findViewById(R.id.id_b);
        id_c=(RadioButton)findViewById(R.id.id_c);
        id_d=(RadioButton) findViewById(R.id.id_d);

        btn_save_marks=(Button)findViewById(R.id.btn_save_marks);

        btn_confirm=(Button)findViewById(R.id.btn_confirm);

        btn_save_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertMarks();
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////
                if (id_a.isChecked()) {
                    String option = id_a.getText().toString();
                    System.out.println("Option A ----------------------- "+option);
                    System.out.println("Correct Option ----------------- "+Model.getInstance().Correct_Option_text);
                    if (option.equals(Model.getInstance().Correct_Option_text)){
                        old_marks = Double.parseDouble(result.getText().toString());
                        new_marks =5.0;
                        sum = old_marks+new_marks;
                        Model.getInstance().Marks =sum;
                        Model.getInstance().model_mcqs_no_testing=Integer.parseInt(txt_quize_mcqs_number.getText().toString());
                        Toast.makeText(StudentQuizSheet.this,"Your Answer is Correct",Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));


                    }
                    else {
                        old_marks = Double.parseDouble(result.getText().toString());
                        //new_marks = 5.0;
                        new_marks = 0.0;
                        sum = old_marks - new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is InCorrect"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));
                    }


                }
                else if (id_b.isChecked()) {
                    String option = id_b.getText().toString();
                    if (option.equals(Model.getInstance().Correct_Option_text)){

                        old_marks = Double.parseDouble(result.getText().toString());
                        new_marks =5.0;

                        sum = old_marks+new_marks;
                        Model.getInstance().Marks =sum;
                        Toast.makeText(StudentQuizSheet.this,"Your Answer is Correct"
                                ,Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));



                    }
                    else {
                        old_marks = Double.parseDouble(result.getText().toString());
                        //new_marks = 5.0;
                        new_marks = 0.0;
                        sum = old_marks - new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is InCorrect"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));
                    }

                }
                else if (id_c.isChecked()) {
                    String option = id_c.getText().toString();
                    if (option.equals(Model.getInstance().Correct_Option_text)){

                        old_marks = Double.parseDouble(result.getText().toString());
                        new_marks =5.0;

                        sum = old_marks + new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is Correct"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));


                    }
                    else {
                        old_marks = Double.parseDouble(result.getText().toString());
                        //new_marks = 5.0;
                        new_marks = 0.0;
                        sum = old_marks - new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is InCorrect"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));
                    }


                }
                else if (id_d.isChecked()) {
                    String option = id_d.getText().toString();

                    if (option.equals(Model.getInstance().Correct_Option_text)) {

                        old_marks = Double.parseDouble(result.getText().toString());
                        new_marks = 5.0;
                        sum = old_marks + new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is Correct"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));
                    }
                    ///////////////////////////////////
                    else {
                        old_marks = Double.parseDouble(result.getText().toString());
                        //new_marks = 5.0;
                        new_marks = 0.0;
                        sum = old_marks - new_marks;
                        Model.getInstance().Marks = sum;
                        Toast.makeText(StudentQuizSheet.this, "Your Answer is InCorrect"
                                , Toast.LENGTH_LONG).show();
                        result.setText(String.valueOf(Model.getInstance().Marks));
                    }

                }

                count++;
                if(count>counter)
                {
                    Toast.makeText(StudentQuizSheet.this, "Question Limit has been Reached", Toast.LENGTH_SHORT).show();
                    btn_confirm.setEnabled(false);
                    btn_confirm.setText("Next - Diabled");
                }
                else
                {
                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^----------Counter: "+counter+"-----------^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^----------Count: "+count+"-----------^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                    //Log.e("count",""+number);
                    txt_quize_mcqs_number.setText(String.valueOf(count));
                    txt_mcqs_txt.setText("");
                    id_a.setText("");
                    id_b.setText("");
                    id_c.setText("");
                    id_d.setText("");

                    retrevingdata();
                }

            }
        });
    }

    /*public void retregingMarksRecords(){

        try {
            retrieving= FirebaseDatabase.getInstance().getReference().child("AUP TEST")
                    .child(Model.getInstance().model_node_name)
                    .child(department)
                    .child(semester)
                    .child(section)
                    .child(Model.getInstance().model_subject_name)
                    .child(Model.getInstance().model_quiz_num);
            System.out.println("===================="+retrieving+"----------------");
            retrieving.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Object> data = new HashMap<>();
                    for (DataSnapshot child:dataSnapshot.getChildren()){

                        data.put(child.getKey(),child.getValue());

                        Log.e("HELO",""+data);
                        try {
                            Toast.makeText(StudentQuizSheet.this,data.toString(),Toast.LENGTH_LONG).show();
                            System.out.println("Retreging Marks Records Method - Values in Data------------------------------"+data.toString());
                            Model.getInstance().test_result=data.get("test_marks").toString();
                            System.out.println("Result of Test_Marks: "+data.get("test_marks").toString()+"----------------");

                        }

                        catch (Exception ae) {
                            ae.printStackTrace();
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception ae){
            ae.printStackTrace();
        }
    }*/

    public void InsertMarks() {

        double zero=0;
        System.out.println("==========================Value of Test_Result: "+Model.getInstance().test_result+"=============================");

        double marks=Double.parseDouble(result.getText().toString());

        System.out.println(section+" Section "+department+" Department "+semester+" Semester ==========================================");

        StudentsMarksModel fb = new StudentsMarksModel(rollnum, String.valueOf(marks), section, department, semester);
        //databaseReference.child("Marks").child(Model.getInstance().model_node_name)
        dbref4.child(department)
                .child(semester)
                .child(section)
                .child(Model.getInstance().model_subject_name)
                .child(Model.getInstance().model_quiz_num)
                .child(rollnum).setValue(fb);
        Toast.makeText(StudentQuizSheet.this, "Thank you: Your Record has been Added", Toast.LENGTH_SHORT).show();
    }

    public void retrevingdata(){
        try {

            txt_quize_mcqs_number.setText(String.valueOf(count));

            /*System.out.println("Model.getInstance().model_node_name: "+Model.getInstance().model_node_name+"============================");
            //System.out.println("Department Test: "+FirebaseDatabase.getInstance().getReference().child("INU").child(Model.getInstance().model_node_name).child(department).toString()+"============================");
            System.out.println("Department Working--------------");

            System.out.println("=====================Department is: "+department+"------------------");
            System.out.println("=====================SEMESTER is: "+semester+"------------------");
            System.out.println("=====================SECTION is: "+section+"------------------");
            System.out.println("=====================SUBJECT is: "+Model.getInstance().model_subject_name+"------------------");
            System.out.println("=====================QUIZ NO. is: "+Model.getInstance().model_quiz_num+"------------------");*/

            //retrievingcount= FirebaseDatabase.getInstance().getReference().child("INU")
            retrievingcount= dbref3
                    .child(department)
                    .child(semester)
                    .child(section)
                    .child(Model.getInstance().model_subject_name)
                    .child(Model.getInstance().model_quiz_num);
            retrievingcount.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("Counter Value: $$$$$$$$$$$$$$$$$$$$$$$$$$$-----------------"+dataSnapshot.getChildrenCount()+"---------------$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    counter = dataSnapshot.getChildrenCount();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            System.out.println("Student Root-----------------------------------------"+Model.getInstance().model_node_name);
            System.out.println("Student department-----------------------------------------"+department);
            System.out.println("Student semester-----------------------------------------"+semester);
            System.out.println("Student section-----------------------------------------"+section);
            System.out.println("Student subject-----------------------------------------"+Model.getInstance().model_subject_name);
            System.out.println("Student quiznum-----------------------------------------"+Model.getInstance().model_quiz_num);
            retrieving= dbref3
                    .child(department)
                    .child(semester)
                    .child(section)
                    .child(Model.getInstance().model_subject_name)
                    .child(Model.getInstance().model_quiz_num)
                    .child(String.valueOf(count));
            System.out.println("===================="+retrieving+"----------------");
            retrieving.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    HashMap<String, Object> data = new HashMap<>();
                    for (DataSnapshot child:dataSnapshot.getChildren()){
                        System.out.println(" Child.getKey^^^^^^^^^^^^^^^^^"+child.getKey()+" ^^^^^^^^^^^^^^^^ Child.getValue ^^^^^^^^^^^^^^^^ "+child.getValue());
                        data.put(child.getKey(),child.getValue());
                    }


                    Log.e("HELO",""+data);
                    try {
                        System.out.println(" RetrevingData Method - Values in Data------------------------------"+data.toString());
                        System.out.println("Result of Option_A is: "+data.get("mcqs_text").toString()+"---------************--------------");
                        txt_mcqs_txt.setText(data.get("mcqs_text").toString());

                        id_a.setText(data.get("option_a").toString());
                        id_b.setText(data.get("option_b").toString());
                        id_c.setText(data.get("option_c").toString());
                        id_d.setText(data.get("option_d").toString());
                        Model.getInstance().Correct_Option_text=data.get("correction_option").toString();
                    }
                    catch (Exception ae) {
                        ae.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception ae){
            ae.printStackTrace();
        }

    }

    }
