 package com.example.iquiz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.Proxy.Type.HTTP;

public class TeacherCreateQuiz extends AppCompatActivity {

    Button submit,btnback,btndate;
    Button btnget;
    Spinner department_spinner, semester_spinner, subject_spinner, section_spinner;
    EditText quiztext,qstime,endhours,quiztime;
    TextView qdate;

    List<SubjectModel> subjects;
    List<String> mysubjects;
    List<QuizModel> quizModels;

    Calendar calendar1,calendar2;

    //int ampmval;

    long startTime,endTime;

    private Uri filePath;
    String fileName;
    private Gson gson;
    HashMap<String, Object> mymap;

    DatabaseReference db = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = db.child("Subjects");
    DatabaseReference dbref2 = db.child("QuizDetails");
    DatabaseReference dbref3 = db.child("Quizes");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_quiz);

        gson = new Gson();

        intialization_widgets();
    }

    public void intialization_widgets() {
        department_spinner=(Spinner)findViewById(R.id.dept_spinner);
        semester_spinner=(Spinner)findViewById(R.id.sem_spinner);
        section_spinner=(Spinner)findViewById(R.id.sec_spinner);
        subject_spinner=(Spinner)findViewById(R.id.sub_spinner);
        //quiz_spinner=(Spinner)findViewById(R.id.ampm_spinner);
        btndate = findViewById(R.id.bdate);
        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeacherCreateQuiz.this,CalendarPage.class);
                startActivityForResult(i,55);

            }
        });

        quiztext = findViewById(R.id.quizserial);
        qstime = findViewById(R.id.quizstarttime);
        qdate = findViewById(R.id.quizdate);
        endhours = findViewById(R.id.endhours);
        quiztime = findViewById(R.id.qtime);

        subjects = new ArrayList<>();
        mysubjects = new ArrayList<>();

        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();

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
                ArrayAdapter<String> spinAdapter1 = new ArrayAdapter<String>(TeacherCreateQuiz.this,android.R.layout.simple_list_item_1,mysubjects);
                subject_spinner.setAdapter(spinAdapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit=(Button)findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String quiznum = quiztext.getText().toString();
                String quizstarttime = qstime.getText().toString();
                //String ampm = quiz_spinner.getSelectedItem().toString();
                String quizdate = qdate.getText().toString();
                String endtime = endhours.getText().toString();
                int totmins = Integer.parseInt(quiztime.getText().toString());

                /*//Calendar cal = Calendar.getInstance();
                System.out.println(myYear);
                System.out.println(curMonth);
                System.out.println(myDate);
                System.out.println(qstartTime);
                System.out.println("==============================================================================");


    System.out.println("ENDIN TIME HOURS-------------------------------------------------"+ehours);
    System.out.println("ENDIN TIME MINUTES-------------------------------------------------"+emins);*/

                //if(!TextUtils.isEmpty(quiznum) && !TextUtils.isEmpty(quizstarttime)  &&!TextUtils.isEmpty(quizdate))
                if(!TextUtils.isEmpty(quiznum) && !TextUtils.isEmpty(quizstarttime))
                {
                    /*if(ampm.equals("AM"))
                    {
                        ampmval = 0;
                    }
                    else
                    {
                        ampmval = 1;
                    }*/

                    int qstartTime = Integer.parseInt(quizstarttime);
                    System.out.println("Quiz Date --------------------------------- "+quizdate);

                    //SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy");
                    Date dt1= null;
                    try {
                        dt1 = format.parse(quizdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //DateFormat format2=new SimpleDateFormat("EEEE");
                    DateFormat format2=new SimpleDateFormat("dd");
                    int myDate= Integer.parseInt(format2.format(dt1));

                    DateFormat format3=new SimpleDateFormat("MM");
                    int myMonth= Integer.parseInt(format3.format(dt1));
                    System.out.println("My Month -------------------------------- "+myMonth);
                    int curMonth = myMonth-1;

                    DateFormat format4=new SimpleDateFormat("yyyy");
                    int myYear= Integer.parseInt(format4.format(dt1));



                    calendar1.set(myYear,curMonth,myDate,qstartTime,0,0);
                    //calendar1.set(myYear,month,cdate,qstartTime,0,0);
                    startTime = calendar1.getTimeInMillis();
                    //Converting into TIME

//                    long shours = TimeUnit.MILLISECONDS.toHours(startTime);
//                    long smins = TimeUnit.MILLISECONDS.toMinutes(startTime);



                    System.out.println("START TIME HOURS-------------------------------------------------"+qstartTime);
                    //System.out.println("START TIME MINUTES-------------------------------------------------"+smins);
                    System.out.println("START TIME YEAR-------------------------------------------------"+myYear);
                    System.out.println("START TIME MONTH-------------------------------------------------"+curMonth);
                    System.out.println("START TIME DAY OF MONTH-------------------------------------------------"+myDate);

                    int ehour = Integer.parseInt(endtime);

                    System.out.println("START TIME HOURS ------------------------------------------------ "+ehour);

                    calendar2.set(myYear,curMonth,myDate,qstartTime+ehour,0,0);
                    //calendar2.set(myYear,month,cdate,qstartTime+ehour,0,0);
                    endTime = calendar2.getTimeInMillis();
                    //Converting into TIME
                    long ehours = TimeUnit.MILLISECONDS.toHours(endTime);
                    long emins = TimeUnit.MILLISECONDS.toMinutes(endTime);


                    Model.getInstance().model_department_name = department_spinner.getSelectedItem().toString();
                    Model.getInstance().model_semester_name = semester_spinner.getSelectedItem().toString();
                    Model.getInstance().model_section_name = section_spinner.getSelectedItem().toString();
                    Model.getInstance().model_subject_name = subject_spinner.getSelectedItem().toString();

                    //Model.getInstance().model_node_name = "Computer Science Test";
                    Model.getInstance().model_quiz_num = quiznum;

                    //QuizTimingModel result = new QuizTimingModel(startTime,endTime);
                    QuizTimingModel result = new QuizTimingModel(startTime,endTime,totmins);
                    //DatabaseReference newref;
                    DatabaseReference newref = dbref2.child(Model.getInstance().model_department_name)
                            .child(Model.getInstance().model_semester_name)
                            .child(Model.getInstance().model_section_name)
                            .child(Model.getInstance().model_subject_name)
                            .child(quiznum);

                    String dbkey = newref.push().getKey();

                    newref.child(dbkey).setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TeacherCreateQuiz.this, "Time Has Been Set", Toast.LENGTH_SHORT).show();
                        }
                    });

                    btnget.setEnabled(true);
                    btnget.setBackground(ContextCompat.getDrawable(TeacherCreateQuiz.this,R.drawable.btn_background_white));

                    submit.setEnabled(false);
                    submit.setBackground(ContextCompat.getDrawable(TeacherCreateQuiz.this,R.drawable.btn_background_gray));

                }
                else
                {
                    Toast.makeText(TeacherCreateQuiz.this, "Fields are Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnback=(Button)findViewById(R.id.btn_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnget = findViewById(R.id.btn_get_quiz);
//        btnget.setEnabled(false);
        btnget.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_background_gray));
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent();
                intent.setType("application/json");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select JSON file"), 555);*/
                Intent intent;

                String[] mimetypes =
                        { "application/vnd.ms-excel", // .xls
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
                        };

                intent = new Intent(Intent.ACTION_GET_CONTENT); // or use ACTION_OPEN_DOCUMENT
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select JSON file"), 555);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==555){
            filePath=data.getData();
            Log.d("TAG", ": result ");
            // Step 1: Read Excel File into Java List Objects
            List customers = readExcelFile(filePath);

            // Step 2: Convert Java Objects to JSON String
            String jsonString = convertObjects2JsonString(customers);

            System.out.println(jsonString);

            try {
                JSONArray result = new JSONArray(jsonString.toString());


                System.out.println("Total Length in JSON ARRAY-------------------------------"+result.length());
                for(int i=0; i<result.length(); i++)
                {
                    HashMap<String,String> parseData = new HashMap<>();
                    JSONObject c = result.getJSONObject(i);
                    String correctopt = c.getString("correct_option");
                    //String deptname = c.getString("department_name");
                    String mcqnum = String.valueOf(c.getInt("mcqs_no"));
                    String mcqtext = c.getString("mcqs_text");
                    String opta = c.getString("option_a");
                    String optb = c.getString("option_b");
                    String optc = c.getString("option_c");
                    String optd = c.getString("option_d");
                    String quiznum = String.valueOf(c.getInt("quiz_num"));
                    //String secname = c.getString("section_name");
                    //String semname = c.getString("semester_name");
                    //String subname = c.getString("subject_name");

                    parseData.put("correction_option",correctopt);
                    //parseData.put("department_name",deptname);
                    parseData.put("mcqs_no",mcqnum);
                    parseData.put("mcqs_text",mcqtext);
                    parseData.put("option_a",opta);
                    parseData.put("option_b",optb);
                    parseData.put("option_c",optc);
                    parseData.put("option_d",optd);
                    parseData.put("quiz_num",quiznum);
                    //parseData.put("section_name",secname);
                    //parseData.put("semester_name",semname);
                    //parseData.put("subject_name",subname);

                    String mykey = String.valueOf(i+1);
                    dbref3.child(Model.getInstance().model_department_name)
                            .child(Model.getInstance().model_semester_name)
                            .child(Model.getInstance().model_section_name)
                            .child(Model.getInstance().model_subject_name)
                            .child(Model.getInstance().model_quiz_num)
                            .child(mykey)
                            .setValue(parseData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode==55)
        {
            qdate.setText(data.getStringExtra("setdate"));
        }
        /*if(requestCode==555)
        {
            filePath = data.getData();
            
            InputStream in = null;


            btnget.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_background_gray));
            btnget.setEnabled(false);
        }
        else if(requestCode==55)
        {
            qdate.setText(data.getStringExtra("setdate"));
        }*/
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private List readExcelFile(Uri uri){

        try {
            AssetManager mgr = TeacherCreateQuiz.this.getAssets();
            File file=new File(uri.toString());

            InputStream excelFile =  getContentResolver().openInputStream(filePath);

//            FileInputStream excelFile= (FileInputStream) mgr.open("customers.xlsx");
            Workbook workbook = new XSSFWorkbook(excelFile);

            Sheet sheet = workbook.getSheet("Transformed by Data.Page");
            Iterator rows = sheet.iterator();

            List lstCustomers = new ArrayList();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = (Row) rows.next();

                // skip header
                if(rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator cellsInRow = currentRow.iterator();

                QuizModel cust = new QuizModel();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = (Cell) cellsInRow.next();

                    if(cellIndex==0) { // ID
                        cust.setCorrect_option(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==1) { // ID
                        cust.setDepartment_name(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==2) { // ID
                        cust.setMcqs_no(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==3) { // ID
                        cust.setMcqs_text(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==4) { // ID
                        cust.setOption_a(String.valueOf(currentCell.getStringCellValue()));
                    }

                    if(cellIndex==5) { // ID
                        cust.setOption_b(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==6) { // ID
                        cust.setOption_c(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==7) { // ID
                        cust.setOption_d(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==8) { // ID
                        cust.setQuiz_num(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==9) { // ID
                        cust.setSection_name(String.valueOf(currentCell.getStringCellValue()));
                    }
                    if(cellIndex==10) { // ID
                        cust.setSemester_name(String.valueOf(currentCell.getStringCellValue()));
                    }if(cellIndex==11) { // ID
                        cust.setSubject_name(String.valueOf(currentCell.getStringCellValue()));
                    }
                    /*else if(cellIndex==1) { // Name
                        cust.setName(currentCell.getStringCellValue());
                    } else if(cellIndex==2) { // Address
                        cust.setAddress(currentCell.getStringCellValue());
                    } else if(cellIndex==3) { // Age
                        cust.setAge((int) currentCell.getNumericCellValue());
                    }*/

                    cellIndex++;
                }

                lstCustomers.add(cust);
            }

            // Close WorkBook

            return lstCustomers;
        } catch (IOException e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

    private static String convertObjects2JsonString(List customers) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = mapper.writeValueAsString(customers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }


}