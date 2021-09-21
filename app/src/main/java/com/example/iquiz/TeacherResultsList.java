package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class TeacherResultsList extends AppCompatActivity implements TeacherResultAdapter.onObjectClickListener, ResultInterface {

    List<TeacherResultModel> mylist;
    RecyclerView myview;
    DatabaseReference db;
    List<TeacherResultModel> name_sub;
    DatabaseReference dbref1;
    private ArrayList<TeacherResultModel> tec=new ArrayList<>();
    ;
    private String stdName;
    private String stdSection;
    private TeacherResultModel teacherResultModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_results_list);

        mylist = new ArrayList<>();
        myview = findViewById(R.id.resultrview);

        LinearLayoutManager lmgr = new LinearLayoutManager(TeacherResultsList.this);
        lmgr.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(lmgr);

        db = FirebaseDatabase.getInstance().getReference("INU");
        dbref1 = db.child("Students");
        Query dbquery = db.child("Marks")

                .child(Model.getInstance().model_department_name)
                .child(Model.getInstance().model_semester_name)
                .child(Model.getInstance().model_section_name)
                .child(Model.getInstance().model_subject_name)
                .child(Model.getInstance().model_quiz_num);
        dbquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot values: dataSnapshot.getChildren())
                {
                    TeacherResultModel record = values.getValue(TeacherResultModel.class);
                    mylist.add(record);
                    Log.d("TAG", "number: ");
                    //System.out.println("List Result ---------***************-------------"+mylist);
              /*      System.out.println("TAG List StudentID ---------***************-------------"+record.getStudent_id());
                    System.out.println("TAG List StudentMarks ---------***************-------------"+record.getTest_marks());
              */
                }

//                System.out.println("List Result ---------***************-------------"+mylist);

                for (int i=0;i<mylist.size();i++){
                    TeacherResultModel teacherResultModel=mylist.get(i);


                    getNames(teacherResultModel.getStudent_id());
                }

                TeacherResultAdapter adapter = new TeacherResultAdapter(mylist,TeacherResultsList.this,TeacherResultsList.this);
                myview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onObjectClick(int position) {
        Toast.makeText(this, "Click is Working", Toast.LENGTH_SHORT).show();
        /*
        Intent i = new Intent(this,Details.class);
        i.putExtra("data",position);
        startActivity(i);
         */
    }


    //    Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(List<TeacherResultModel> mlist) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("TAG", "here before file: ");

            File dir = new File(TeacherResultsList.this.getFilesDir(), "newfile");
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

            Paragraph p1 = new Paragraph("IQRA UNIVERSITY OF SCIENCE AND INFORMATION TECHNOLOGY");
            Font paraFont= new Font();
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(paraFont);



        /*    // Create a Simple table
            PdfPTable table = new PdfPTable(3);


            // Set First row as header
            table.setHeaderRows(1);
            // Add header details
            table.addCell("STD ID");
            table.addCell("Name");
            table.addCell("Section");
            table.addCell("Marks");
            table.addCell("Result");
*/
            // Create a Simple table
            PdfPTable table = new PdfPTable(5);

            // Set First row as header
            table.setHeaderRows(1);
            // Add header details
            table.addCell("STD ID");
            table.addCell("Name");
            table.addCell("Section");
            table.addCell("Marks");
            table.addCell("Result");

            for (int i=0;i<mylist.size();i++){
                // Add the data
                TeacherResultModel resultModel=mylist.get(i);
//                TeacherResultModel resultModel2=name_sub.get(i);
                table.addCell(resultModel.student_id);
                table.addCell(tec.get(i).getName());
                table.addCell(tec.get(i).getSub());
                table.addCell(resultModel.getTest_marks());
//                table.addCell("Fail");

                Double smarks = Double.parseDouble(resultModel.getTest_marks());
//                int markes= Integer.parseInt(resultModel.getTest_marks());
                if (smarks<=5){
                    table.addCell("Fail");

                }
                else {
                    table.addCell("Pass");

                }
            }





           /* for (int i=0;i<mylist.size();i++){



                TeacherResultModel result=mylist.get(i);

                 p2 = new Paragraph("ID :"+result.getName());
                Font paraFont2= new Font();
                p2.setAlignment(Paragraph.ALIGN_CENTER);
                p2.setFont(paraFont2);
            }*/



            // close the document


            //add paragraph to document
            doc.add(p1);
            doc.add(Chunk.NEWLINE);
            doc.add(table);
            doc.close();

            //add paragraph to document

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
        }

        viewPdf("newFile.pdf","");
    }


    private void viewPdf(String file, String directory) {



        File dir = new File(TeacherResultsList.this.getFilesDir(), "newfile");
        File file2= new File(dir, "newFile.pdf");





        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
        if (file2.exists()) {
            intentShareFile.setType("application/pdf");
            Uri data = FileProvider.getUriForFile(TeacherResultsList.this, BuildConfig.APPLICATION_ID + ".provider" ,file2);

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

    public void download(View view) {
        createandDisplayPdf(mylist);}


    @Override
    public void data(List<TeacherResultModel> model) {

      name_sub = new ArrayList<TeacherResultModel>(model);


        for (int i=0;i<model.size();i++){
        TeacherResultModel model1=name_sub.get(i);
        tec.add(model1);
      /*  for (int i=0;i<model.size();i++){
            TeacherResultModel teacherResultModel=model.get(i);
            name_sub.add(teacherResultModel);
        }*/
    }}


    public void getNames(String id){

        int uid = Integer.parseInt(id);

        Query qry = dbref1.orderByChild("roll").equalTo(uid);
        qry.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot values : dataSnapshot.getChildren()) {
                    StudentModel records = values.getValue(StudentModel.class);
                /*    System.out.println("Student Name: -------------------------------------------------"+records.getName());
                    System.out.println("Section Name: -------------------------------------------------"+records.getSection());
                   */ stdName = records.getName();
                    stdSection = records.getSection();

                    Log.d("TAG", "see : "+stdName);
                     teacherResultModel=new TeacherResultModel();
                    teacherResultModel.setName(records.getName());
                    teacherResultModel.setSub(records.getSection());
                    tec.add(teacherResultModel);

                }
                for (int i=0;i<tec.size();i++){
                    Log.d("TAG", "name array size: "+i+" : "+tec.get(i).getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}