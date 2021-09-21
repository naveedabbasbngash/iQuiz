package com.example.iquiz;

public class Model {

    public String model_department_name;
    public String model_semester_name;
    public String model_section_name;
    public String model_subject_name;

    public String model_quiz_num;

    public String model_node_name;
    public String model_mcqs_no;
    public int model_mcqs_no_testing;
    public String model_msqs_text;
    public  String test_result;
    public String model_option_a;
    public String model_option_b;
    public String model_option_c;
    public String model_option_d;
    public String model_currect_option;
    public String Correct_Option_text;
    public Double Marks;

    private static final Model ourInstance = new Model();

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }
}
