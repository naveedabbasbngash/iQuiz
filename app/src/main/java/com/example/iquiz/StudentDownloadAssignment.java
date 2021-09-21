package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class StudentDownloadAssignment extends AppCompatActivity implements View.OnClickListener {

    Button btnchoose,btndownload, btnlist;
    TextView txtStatus;
    EditText assignment;

    List<SubjectModel> subjects;
    List<String> mysubjects;

    Spinner department_spinner, semester_spinner, subject_spinner, section_spinner;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Subjects");

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference sr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_download_assignment);

        btnlist = findViewById(R.id.listassign);
        btnlist.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        department_spinner=(Spinner)findViewById(R.id.dept_spinner);
        semester_spinner=(Spinner)findViewById(R.id.sem_spinner);
        section_spinner=(Spinner)findViewById(R.id.sec_spinner);
        subject_spinner=(Spinner)findViewById(R.id.sub_spinner);

        System.out.println("Student Department: ------------------------------------------------------ "+department_spinner.getSelectedItem().toString());
        System.out.println("Student Semester: ------------------------------------------------------ "+semester_spinner.getSelectedItem().toString());
        System.out.println("Student Section: ------------------------------------------------------ "+section_spinner.getSelectedItem().toString());
        //System.out.println("Student Subject: ------------------------------------------------------ "+subject_spinner.getSelectedItem().toString());

        subjects = new ArrayList<>();
        mysubjects = new ArrayList<>();

        dbref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    SubjectModel records = values.getValue(SubjectModel.class);
                    subjects.add(records);
                }

                for(int i=0; i<subjects.size(); i++)
                {
                    mysubjects.add(subjects.get(i).getSubject());
                }
                ArrayAdapter<String> spinAdapter1 = new ArrayAdapter<String>(StudentDownloadAssignment.this,android.R.layout.simple_list_item_1,mysubjects);
                subject_spinner.setAdapter(spinAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.listassign)
        {
            Model.getInstance().model_department_name = department_spinner.getSelectedItem().toString();
            Model.getInstance().model_semester_name = semester_spinner.getSelectedItem().toString();
            Model.getInstance().model_section_name = section_spinner.getSelectedItem().toString();
            Model.getInstance().model_subject_name = subject_spinner.getSelectedItem().toString();

            Intent i = new Intent(this, TeacherAssignmentList.class);
            startActivity(i);
        }
    }
}