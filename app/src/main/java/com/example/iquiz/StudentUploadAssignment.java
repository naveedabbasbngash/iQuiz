package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class StudentUploadAssignment extends AppCompatActivity implements View.OnClickListener {

    Button btnchoose,btnupload,btnback;
    TextView txtStatus;
    EditText assignment;

    List<SubjectModel> subjects;
    List<String> mysubjects;

    Spinner department_spinner, semester_spinner, subject_spinner, section_spinner;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Subjects");
    DatabaseReference dbref2 = db.child("Assigments").child("Student Uploaded");

    final static int PICK_PDF_CODE = 555;

    private Uri filePath;

    private StorageReference storageReference;
    SharedPreferences sp;
    int roll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_assignment);

        sp = getSharedPreferences("myappdata", this.MODE_PRIVATE);
        roll = sp.getInt("sroll", 555);

        btnchoose = findViewById(R.id.chooseassign);
        btnupload = findViewById(R.id.uploadassign);
        btnback = findViewById(R.id.stdback);

        btnchoose.setOnClickListener(this);
        btnupload.setOnClickListener(this);
        btnback.setOnClickListener(this);

        txtStatus = findViewById(R.id.txtstatus);
        assignment = findViewById(R.id.nameassign);

        storageReference = FirebaseStorage.getInstance().getReference();

        department_spinner=(Spinner)findViewById(R.id.dept_spinner);
        semester_spinner=(Spinner)findViewById(R.id.sem_spinner);
        section_spinner=(Spinner)findViewById(R.id.sec_spinner);
        subject_spinner=(Spinner)findViewById(R.id.sub_spinner);

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
                ArrayAdapter<String> spinAdapter1 = new ArrayAdapter<String>(StudentUploadAssignment.this,android.R.layout.simple_list_item_1,mysubjects);
                subject_spinner.setAdapter(spinAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.chooseassign)
        {
            chooseFile();
        }
        else if(view.getId()==R.id.uploadassign)
        {
            String quizname = assignment.getText().toString();
            if(!TextUtils.isEmpty(quizname)) {
                Model.getInstance().model_department_name = department_spinner.getSelectedItem().toString();
                Model.getInstance().model_semester_name = semester_spinner.getSelectedItem().toString();
                Model.getInstance().model_section_name = section_spinner.getSelectedItem().toString();
                Model.getInstance().model_subject_name = subject_spinner.getSelectedItem().toString();
                uploadFile();
            }
        }
        else if(view.getId()==R.id.stdback)
        {
            finish();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        //intent.setType("image/*");
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            String fullname = assignment.getText().toString()+".pdf";
            final StorageReference riversRef = storageReference.child("Assigments").child("Student Uploaded")
                    .child(Model.getInstance().model_department_name)
                    .child(Model.getInstance().model_subject_name)
                    .child(Model.getInstance().model_semester_name)
                    .child(Model.getInstance().model_section_name)
                    .child(String.valueOf(roll))
                    .child(fullname);


            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            /////////////////////////////////////////////////////
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    System.out.println("Path is--------------------------------------------------------------"+uri);
                                    String fullname = assignment.getText().toString()+".pdf";
                                    System.out.println("---------------------------- "+fullname+" --------------------------------");

                                    String stdroll = String.valueOf(roll);
                                    AssignmentModel upload = new AssignmentModel(fullname, uri.toString(),stdroll);
                                    //gs://oexam-3c3e5.appspot.com/Assigments/CS/Artificial Intelligence/SEMESTER1/A
                                    /*dbref2 = dbref2.child(Model.getInstance().model_department_name)
                                            .child(Model.getInstance().model_subject_name)
                                            .child(Model.getInstance().model_semester_name)
                                            .child(Model.getInstance().model_section_name)
                                            .child(String.valueOf(roll));*/

                                    dbref2 = dbref2.child(Model.getInstance().model_department_name)
                                            .child(Model.getInstance().model_subject_name)
                                            .child(Model.getInstance().model_semester_name)
                                            .child(Model.getInstance().model_section_name);

                                    String mykey = dbref2.push().getKey();
                                    dbref2.child(mykey).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            System.out.println("Record has been Added...");
                                            Toast.makeText(StudentUploadAssignment.this, "Assigment has been Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    assignment.setText("");
                                    txtStatus.setText("");
                                }
                            });
                            //////////////////////////////////////////////////////
                            //Also Put File URL into Firebase Database

                            /*String fullname = assignment.getText().toString()+".pdf";
                            System.out.println("---------------------------- "+fullname+" --------------------------------");
                            AssignmentModel upload = new AssignmentModel(fullname, taskSnapshot.getDownloadUrl().toString());
                            //gs://oexam-3c3e5.appspot.com/Assigments/CS/Artificial Intelligence/SEMESTER1/A
                            dbref2 = dbref2.child(Model.getInstance().model_department_name)
                                    .child(Model.getInstance().model_subject_name)
                                    .child(Model.getInstance().model_semester_name)
                                    .child(Model.getInstance().model_section_name);

                            String mykey = dbref2.push().getKey();
                            dbref2.child(mykey).setValue(upload).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("Record has been Added...");
                                    Toast.makeText(StudentUploadAssignment.this, "Assigment has been Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                            assignment.setText("");
                            txtStatus.setText("");*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }

        else {
            //you can display an error toast
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(data.getData()!=null)
            {
                //uploadFile(data.getData());
                filePath = data.getData();
                txtStatus.setText("File Selected");
            }

        }
        else
        {
            txtStatus.setText("No File Selected");
        }
    }
}