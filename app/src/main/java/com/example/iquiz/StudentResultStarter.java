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
import android.util.Log;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StudentResultStarter extends AppCompatActivity implements View.OnClickListener{
    Button btnResult, backbtn;
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

    long startTim,endTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result_starter);

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

        btnResult = findViewById(R.id.showmarks);
        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(this);

        btnResult.setOnClickListener(this);
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
                ArrayAdapter<String> spinAdapter1 = new ArrayAdapter<String>(StudentResultStarter.this, android.R.layout.simple_list_item_1, mysubjects);
                subject_spinner.setAdapter(spinAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.showmarks) {

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

            //String CurrentDate = String.valueOf(Calendar.getInstance().getTimeInMillis());

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
            final long currentTIM = calendar.getTimeInMillis();
            System.out.println("----------------------Current Time In Millis ----------------------------------------------"+currentTIM);

            if (!TextUtils.isEmpty(quiztxt)) {
                //Model.getInstance().model_node_name = "Computer Science Test";
                Model.getInstance().model_subject_name = subject_spinner.getSelectedItem().toString();
                System.out.println("Subject Name is: " + Model.getInstance().model_subject_name + "=====================");
                System.out.println("Quiz Number from EDIT TEXT is: " + quizText.getText().toString() + "=====================");
                Model.getInstance().model_quiz_num = quizText.getText().toString();
                System.out.println("Quiz Number is: " + Model.getInstance().model_quiz_num + "=====================");


                //dept,sect,sems,roll;

                /*Query qdbtime = dbref3.child(dept)
                        .child(sems)
                        .child(sect)
                        .child(Model.getInstance().model_subject_name)
                        .child(Model.getInstance().model_quiz_num);*/

                Query qdbtime = dbref3.child(dept)
                        .child(sems)
                        .child(sect)
                        .child(Model.getInstance().model_subject_name)
                        .child(Model.getInstance().model_quiz_num);

                qdbtime.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String spn_value=subject_spinner.getSelectedItem().toString();
                            Log.d("TAG", ":spinner sub "+spn_value);
                            quizText.setText("");
                            Intent c = new Intent(StudentResultStarter.this, StudentResult.class)
                                    .putExtra("sub_name",spn_value)
                                    .putExtra("quiz_no",quizText.getText().toString());
                            startActivity(c);
                        }
                        else
                        {
                            Toast.makeText(StudentResultStarter.this, "Quiz Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                /*qdbtime.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot values : dataSnapshot.getChildren())
                        {
                            QuizTimingModel records = values.getValue(QuizTimingModel.class);
                            startTim = records.getQuizStartTime();
                            endTim = records.getQuizEndTime();
                            System.out.println("Start Time=========================================================================" + records.getQuizStartTime());
                            System.out.println("End Time=========================================================================" + records.getQuizEndTime());
                            System.out.println("Current Time=========================================================================" + records.getQuizEndTime());

                            if(currentTIM>=startTim && currentTIM<=endTim)
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
                                            Toast.makeText(StudentResultStarter.this, "Already Atempted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent c = new Intent(StudentResultStarter.this, StudentResult.class);
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
                                Toast.makeText(StudentResultStarter.this, "Sorry! Exam Session has been expired or not Started", Toast.LENGTH_SHORT).show();
                                System.out.println();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/


                System.out.println("---------------------------------------------------------------------------------");

            }
            else {
                Toast.makeText(this, "Fields or Empty", Toast.LENGTH_SHORT).show();
            }
        }
        else if(view.getId()==R.id.btn_back)
        {
            startActivity(new Intent(this,StudentPanel.class));
            finish();
        }
    }


    //    Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(String text) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("TAG", "here before file: ");

            File dir = new File(StudentResultStarter.this.getFilesDir(), "newfile");
            Log.d("TAG", "here after file: ");
            if(!dir.exists())
                dir.mkdirs();{
                Log.d("TAG", "here checking not exisance: ");

            }
            Log.d("TAG", "here checking existence: ");
            File file = new File(dir, "newFile.pdf");

            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();

            Paragraph p1 = new Paragraph(text);
            Font paraFont= new Font();
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);

            //add paragraph to document
            doc.add(p1);

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }

        viewPdf("newFile.pdf","");
    }


    private void viewPdf(String file, String directory) {


        File dir = new File(StudentResultStarter.this.getFilesDir(), "newfile");
        File file2= new File(dir, "newFile.pdf");





        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
        if (file2.exists()) {
            intentShareFile.setType("application/pdf");
            Uri data = FileProvider.getUriForFile(StudentResultStarter.this, BuildConfig.APPLICATION_ID + ".provider" ,file2);

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(data, "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(),
                        "There is no any PDF Viewer",
                        Toast.LENGTH_SHORT).show();
                finish();
            }}}
}