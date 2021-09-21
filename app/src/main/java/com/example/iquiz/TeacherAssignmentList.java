package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherAssignmentList extends AppCompatActivity {

    ListView listView;

    //DatabaseReference dbDownload = FirebaseDatabase.getInstance().getReference("INU").child("Assigments").child("Teacher Uploaded");
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Assigments");
    List<AssignmentModel> uploadList;
    ArrayList<String> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignment_list);

        uploadList = new ArrayList<>();
        fileList = new ArrayList<>();
        listView = findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the upload
                AssignmentModel download = uploadList.get(i);

                //Opening the upload file in browser using the upload url
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(download.getAssignPath()));
                startActivity(intent);
            }
        });

        //DatabaseReference mDatabaseReference = dbDownload.child(Model.getInstance().model_department_name).child(Model.getInstance().model_subject_name).child(Model.getInstance().model_semester_name).child(Model.getInstance().model_section_name);
        //Query query = dbref1.child("Teacher Uploaded").child("CS").child("AI").child("SEMESTER1").child("A");
        //mDatabaseReference.addValueEventListener(new ValueEventListener() {
        Query query = dbref1.child("Teacher Uploaded").child(Model.getInstance().model_department_name).child(Model.getInstance().model_subject_name).child(Model.getInstance().model_semester_name).child(Model.getInstance().model_section_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AssignmentModel upload = postSnapshot.getValue(AssignmentModel.class);
                    uploadList.add(upload);
                    fileList.add(upload.getAssignName());
                }

                /*String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getAssignName();
                }
*/
                //displaying it to list
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherAssignmentList.this, android.R.layout.simple_list_item_1, uploads);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherAssignmentList.this, android.R.layout.simple_list_item_1, fileList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
}