package com.example.iquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StudentResult extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String roll,semester,department,section;
    TextView txtmarks,txtstatus;
    Button back;


    DatabaseReference retrieving,retrievingcount;
    DatabaseReference databaseReference;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");

    DatabaseReference dbref4  = db.child("Marks");
    String sub_name,quiz_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        sharedPreferences = getSharedPreferences("myappdata",this.MODE_PRIVATE);
        String username = sharedPreferences.getString("stoken","INVALID");
        semester = sharedPreferences.getString("ssem","INVALID");
        department = sharedPreferences.getString("sdept","INVALID");
        section = sharedPreferences.getString("ssection","INVALID");
        int sroll = sharedPreferences.getInt("sroll",5555);

        roll = String.valueOf(sroll);
        back = findViewById(R.id.adback);

        sub_name=getIntent().getStringExtra("sub_name");
        quiz_no=getIntent().getStringExtra("quiz_no");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentResult.this,StudentResultStarter.class));
                finish();
            }
        });

        txtmarks = findViewById(R.id.rsltmarks);
        txtstatus = findViewById(R.id.rsltstatus);

        dbref4.child(department)
                .child(semester)
                .child(section)
                .child(Model.getInstance().model_subject_name)
                .child(Model.getInstance().model_quiz_num).child(roll).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String marks = snapshot.child("test_marks").getValue().toString();
                txtmarks.setText(marks);
                Double stdmarks = Double.parseDouble(marks);
                if(stdmarks<=10)
                {
                    txtstatus.setBackgroundColor(Color.RED);
                    txtstatus.setText("FAIL");
                }
                else
                {
                    txtstatus.setBackgroundColor(Color.GREEN);
                    txtstatus.setText("PASS");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    //    Method for creating a pdf file from text, saving it then opening it for display
    public void createandDisplayPdf(String sub_name,String quiz_no,String txtmarks,String txtstatus) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("TAG", "here before file: ");

            File dir = new File(StudentResult.this.getFilesDir(), "newfile");
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





            PdfPTable table = new PdfPTable(1);
            table.setSplitRows(false);
            PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            PdfPCell c2 = new PdfPCell(new Phrase("Subjet: "+sub_name));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(c2);


            //add paragraph to document
            doc.add(p1);
            doc.add(Chunk.NEWLINE);
            doc.add(table);


            //add paragraph to document

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


        File dir = new File(StudentResult.this.getFilesDir(), "newfile");
        File file2= new File(dir, "newFile.pdf");





        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
        if (file2.exists()) {
            intentShareFile.setType("application/pdf");
            Uri data = FileProvider.getUriForFile(StudentResult.this, BuildConfig.APPLICATION_ID + ".provider" ,file2);

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

    public void showpdf(View view) {
        createandDisplayPdf(sub_name,quiz_no,txtmarks.getText().toString(),txtstatus.getText().toString());

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}