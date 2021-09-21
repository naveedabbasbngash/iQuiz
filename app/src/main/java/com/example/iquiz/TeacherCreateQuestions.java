package com.example.iquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherCreateQuestions extends AppCompatActivity {

    EditText edt_enter_mcqs_number, edt_enter_mcqs_txt,edt_enter_option_a,
            edt_enter_option_b,edt_enter_option_c,
            edt_enter_option_d,edt_enter_correct_option;
    Button btn_next,btn_back;
    int counter=1;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("INU");
    DatabaseReference dbref1 = databaseReference.child("Quizes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_create_questions);

        Initalization_widgets();
        //databaseReference= FirebaseDatabase.getInstance().getReference("INU");
    }

    public void Initalization_widgets() {
        btn_next =(Button)findViewById(R.id.btn_next);
        btn_back =(Button)findViewById(R.id.btn_back);

        edt_enter_mcqs_number=(EditText) findViewById(R.id.edt_enter_mcqs_number);
        edt_enter_mcqs_txt=(EditText)findViewById(R.id.edt_enter_mcqs_txt);
        edt_enter_option_a=(EditText)findViewById(R.id.edt_enter_option_a);
        edt_enter_option_b=(EditText)findViewById(R.id.edt_enter_option_b);
        edt_enter_option_c=(EditText)findViewById(R.id.edt_enter_option_c);
        edt_enter_option_d=(EditText)findViewById(R.id.edt_enter_option_d);
        edt_enter_correct_option=(EditText)findViewById(R.id.edt_enter_correct_option);

        edt_enter_mcqs_number.setText(String.valueOf(counter));

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quizid = edt_enter_mcqs_number.getText().toString();
                if(!TextUtils.isEmpty(quizid))
                {
                    Insertion_data();
                }
                else
                {
                    Toast.makeText(TeacherCreateQuestions.this, "Please Enter Quiz Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void Insertion_data() {

        Model.getInstance().model_mcqs_no=edt_enter_mcqs_number.getText().toString();
        Model.getInstance().model_msqs_text=edt_enter_mcqs_txt.getText().toString();
        Model.getInstance().model_option_a=edt_enter_option_a.getText().toString();
        Model.getInstance().model_option_b=edt_enter_option_b.getText().toString();
        Model.getInstance().model_option_c=edt_enter_option_c.getText().toString();
        Model.getInstance().model_option_d=edt_enter_option_d.getText().toString();
        Model.getInstance().model_currect_option=edt_enter_correct_option.getText().toString();
        QuizModel firebasejob = new QuizModel(Model.getInstance().model_department_name,
                Model.getInstance().model_semester_name,
                Model.getInstance().model_section_name,
                Model.getInstance().model_subject_name,
                Model.getInstance().model_quiz_num,
                Model.getInstance().model_mcqs_no,
                Model.getInstance().model_msqs_text,
                Model.getInstance().model_option_a,
                Model.getInstance().model_option_b,
                Model.getInstance().model_option_c,
                Model.getInstance().model_option_d,
                Model.getInstance().model_currect_option);
        //databaseReference.child(Model.getInstance().model_node_name)
        dbref1.child(Model.getInstance().model_department_name)
                .child(Model.getInstance().model_semester_name)
                .child(Model.getInstance().model_section_name)
                .child(Model.getInstance().model_subject_name)
                .child(Model.getInstance().model_quiz_num)
                .child(Model.getInstance().model_mcqs_no)
                .setValue(firebasejob);

        Toast.makeText(this, "Question has been Added...", Toast.LENGTH_SHORT).show();
        //edt_enter_correct_option;
        edt_enter_mcqs_number.getText().clear();
        edt_enter_mcqs_txt.getText().clear();
        edt_enter_option_a.getText().clear();
        edt_enter_option_b.getText().clear();
        edt_enter_option_c.getText().clear();
        edt_enter_option_d.getText().clear();
        edt_enter_correct_option.getText().clear();

        edt_enter_mcqs_txt.requestFocus();
        counter++;
        if(counter!=1)
        {
            edt_enter_mcqs_number.setText(String.valueOf(counter));
        }
    }
}