package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StudentQuizStarter extends AppCompatActivity implements View.OnClickListener {

    Button startQuiz, backbtn;
    Spinner subject_spinner;
    EditText quizText;

    String dept, sect, sems;
    int roll;

    List<SubjectModel> subjects;
    List<String> mysubjects;

    Boolean working = false;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Subjects");
    DatabaseReference dbref2 = db.child("Marks");
    DatabaseReference dbref3 = db.child("QuizDetails");

    SharedPreferences sp;
    Calendar calendar;
    Calendar calendar1;

    long startTim,endTim;
    int totMins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_quiz_starter);

        sp = getSharedPreferences("myappdata", this.MODE_PRIVATE);
        roll = sp.getInt("sroll", 555);
        dept = sp.getString("sdept", null);
        sect = sp.getString("ssection", null);
        sems = sp.getString("ssem", null);

        System.out.println("Student Department --------------- "+dept);
        System.out.println("Student Section --------------- "+sect);
        System.out.println("Student Semester --------------- "+sems);

        intialized_button();
    }

    private void intialized_button() {

        calendar = Calendar.getInstance();
        calendar1 = Calendar.getInstance();

        startQuiz = findViewById(R.id.quizstart);
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(this);

        startQuiz.setOnClickListener(this);
        subject_spinner = findViewById(R.id.sub_spinner);
        quizText = findViewById(R.id.quiznum);

        subjects = new ArrayList<>();
        mysubjects = new ArrayList<>();


        dbref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    SubjectModel records = values.getValue(SubjectModel.class);
                    subjects.add(records);
                    System.out.println(records.getSubject());
                }
                for (int i = 0; i < subjects.size(); i++) {
                    mysubjects.add(subjects.get(i).getSubject());
                }
                ArrayAdapter<String> spinAdapter1 = new ArrayAdapter<String>(StudentQuizStarter.this, android.R.layout.simple_list_item_1, mysubjects);
                subject_spinner.setAdapter(spinAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.quizstart) {

            System.out.println("==============================================================================");
            //Calendar cal = Calendar.getInstance();
            System.out.println(calendar.get(Calendar.YEAR));
            System.out.println(calendar.get(Calendar.MONTH));
            System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
            System.out.println(calendar.get(Calendar.HOUR));
            System.out.println(calendar.get(Calendar.MINUTE));
            System.out.println(calendar.get(Calendar.SECOND));
            System.out.println("==============================================================================");

            String quiztxt = quizText.getText().toString();

            //////////////////////////// - Get Current Time in Millis \\\\\\\\\\\\\\\\\\\\\\\\\\\\\

//            SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
//            String dt1= null;
//            try {
//                Date date = new Date();
//                String dt1 = formatter.format(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            //DateFormat format2=new SimpleDateFormat("EEEE");


            /*SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            //String dt1 = formatter.format(date);
            System.out.println("Current Date ------------------------------------------ "+formatter.format(date));

            DateFormat format2=new SimpleDateFormat("dd");
            int myDate= Integer.parseInt(format2.format(formatter.format(date)));

            DateFormat format3=new SimpleDateFormat("MM");
            int myMonth= Integer.parseInt(format3.format(formatter.format(date)));
            int curMonth = myMonth-1;

            DateFormat format4=new SimpleDateFormat("yyyy");
            int myYear= Integer.parseInt(format4.format(formatter.format(date)));

            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);*/

            //calendar1.set(myYear,curMonth,myDate,currentHour,0,0);

            /////////////////////////////////////
            Date date1 = new Date();
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            int cmonth = cal1.get(Calendar.MONTH);
            int cdate = cal1.get(Calendar.DAY_OF_MONTH);
            int cyear = cal1.get(Calendar.YEAR);
            int chour = cal1.get(Calendar.HOUR_OF_DAY);
            int cmin = cal1.get(Calendar.MINUTE);
            int csec = cal1.get(Calendar.SECOND);

            System.out.println("Calendar TEST ---------------- Current Month ---------------- "+cmonth);
            System.out.println("Calendar TEST ---------------- Current Date ---------------- "+cdate);
            System.out.println("Calendar TEST ---------------- Current Year ---------------- "+cyear);
            /////////////////////////////////////


            //calendar1.set(2021,7,11,2,0,0);
            //calendar1.set(cyear,cmonth,cdate,chour,0,0);
            calendar1.set(cyear,cmonth,cdate,chour,cmin,csec);
            final long currentTimeinMilli = calendar1.getTimeInMillis();
            System.out.println("Current Time in Millis Level 1-----------------------------------------------"+currentTimeinMilli);
            System.out.println("Current Time in Millis Level 2-----------------------------------------------"+currentTimeinMilli);
            System.out.println("Current Time in Millis Level 3-----------------------------------------------"+currentTimeinMilli);
            //Converting into TIME
            //long shours = TimeUnit.MILLISECONDS.toHours(startTime);
            //long smins = TimeUnit.MILLISECONDS.toMinutes(startTime);
            //System.out.println("START TIME HOURS-------------------------------------------------"+shours);
            //System.out.println("START TIME MINUTES-------------------------------------------------"+smins);
            //System.out.println("START TIME YEAR-------------------------------------------------"+myYear);
            //System.out.println("START TIME MONTH-------------------------------------------------"+curMonth);
            //System.out.println("START TIME DAY OF MONTH-------------------------------------------------"+myDate);

            //int ehour = Integer.parseInt(endtime);

            //calendar2.set(myYear,curMonth,myDate,qstartTime+ehour,0,0);

            //////////////////////////////////////// - END \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

            //String CurrentDate = String.valueOf(Calendar.getInstance().getTimeInMillis());

//            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
//            final long currentTIM = calendar.getTimeInMillis();
//            Date date = new Date(currentTIM);
//            System.out.println("Current Date  ------------------------- "+ DateFormat.getDateInstance().format(date));
//            System.out.println("Current Time ------------------------- "+ DateFormat.getDateInstance().format(date));
//            System.out.println("Current Hours ----------------------- "+TimeUnit.MILLISECONDS.toHours(currentTIM));
//            System.out.println("Current Time YEAR ----------------------------------------------"+Calendar.YEAR);
//            System.out.println("Current Time MONTH ----------------------------------------------"+Calendar.MONTH);
//            System.out.println("Current Time DAY OF MONTH ----------------------------------------------"+Calendar.DAY_OF_MONTH);
//            System.out.println("Current Time HOUR ----------------------------------------------"+Calendar.HOUR);
//            System.out.println("Current Time MINUTE ----------------------------------------------"+Calendar.MINUTE);
//            System.out.println("----------------------Current Time In Millis ----------------------------------------------"+currentTIM);

            if (!TextUtils.isEmpty(quiztxt)) {
                //Model.getInstance().model_node_name = "Computer Science Test";
                Model.getInstance().model_subject_name = subject_spinner.getSelectedItem().toString();
                System.out.println("Subject Name is: " + Model.getInstance().model_subject_name + "=====================");
                System.out.println("Quiz Number from EDIT TEXT is: " + quizText.getText().toString() + "=====================");
                Model.getInstance().model_quiz_num = quizText.getText().toString();
                System.out.println("Quiz Number is: " + Model.getInstance().model_quiz_num + "=====================");


                //dept,sect,sems,roll;

                Query qdbtime = dbref3.child(dept)
                        .child(sems)
                        .child(sect)
                        .child(Model.getInstance().model_subject_name)
                        .child(Model.getInstance().model_quiz_num);
                qdbtime.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot values : dataSnapshot.getChildren())
                        {
                            QuizTimingModel records = values.getValue(QuizTimingModel.class);
                            startTim = records.getQuizStartTime();
                            endTim = records.getQuizEndTime();
                            totMins = records.getQuizDuration();
                            System.out.println("Start Time - Level =========================================================================" + records.getQuizStartTime());
                            System.out.println("Current Time - Level =========================================================================" + currentTimeinMilli);
                            System.out.println("End Time - Level =========================================================================" + records.getQuizEndTime());

                            System.out.println("Fetch Info - Date ---------------------------- "+ TimeUnit.MILLISECONDS.toDays(startTim));
                            /*System.out.println("Fetch Info - Date ---------------------------- "+ SimpleDateFormat("hh:mm:ss"));
                            System.out.println("Fetch Info - Date ---------------------------- "+ TimeUnit.DAYS.toMillis(startTim));
                            System.out.println("Fetch Info - Date ---------------------------- "+ TimeUnit.DAYS.toMillis(startTim));*/

                            //if(currentTIM>=startTim && currentTIM<=endTim)
                            if(currentTimeinMilli>=startTim && currentTimeinMilli<=endTim)
                            {
                                System.out.println();
                                System.out.println("------------You are elegible to do the exam-------------");
                                System.out.println("--------------- Start of Nested Firebase ---------------");

                                String stdroll = String.valueOf(roll);

                                //Query qdb = dbref2.child(Model.getInstance().model_node_name)
                                Query qdb = dbref2
                                        .child(dept)
                                        .child(sems)
                                        .child(sect)
                                        .child(Model.getInstance().model_subject_name)
                                        .child(Model.getInstance().model_quiz_num)
                                        .child(stdroll);

                                qdb.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            working = true;
                                        } else {
                                            working = false;
                                        }

                                        if (working) {
                                            Toast.makeText(StudentQuizStarter.this, "Already Atempted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent c = new Intent(StudentQuizStarter.this, StudentQuizSheet.class);
                                            c.putExtra("totmins",totMins);
                                            startActivity(c);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                System.out.println("------------------ End of Nested Firebase -------------------");
                            }
                            else
                            {
                                System.out.println();
                                System.out.println("----------You are not elegible to do the exam----------");
                                Toast.makeText(StudentQuizStarter.this, "Sorry! Exam Session has been expired or not Started", Toast.LENGTH_SHORT).show();
                                System.out.println();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                System.out.println("---------------------------------------------------------------------------------");

            }
            else {
                Toast.makeText(this, "Fields or Empty", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId()==R.id.btn_back)
        {
            finish();
        }
    }
}