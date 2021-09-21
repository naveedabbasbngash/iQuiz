package com.example.iquiz;

public class QuizModel {

    public String department_name;
    public String semester_name;
    public String section_name;
    public String subject_name;

    public String quiz_num;

    public String mcqs_no;
    public String mcqs_text;
    public String option_a;
    public String option_b;
    public String option_c;
    public String option_d;
    public String currect_option;
    private String correct_option;

    public QuizModel()
    {

    }

    public QuizModel(String department_name, String semester_name, String section_name, String subject_name, String quiz_num, String mcqs_no, String mcqs_text, String option_a, String option_b, String option_c, String option_d, String currect_option) {
        this.department_name = department_name;
        this.semester_name = semester_name;
        this.section_name = section_name;
        this.subject_name = subject_name;
        this.quiz_num = quiz_num;
        this.mcqs_no = mcqs_no;
        this.mcqs_text = mcqs_text;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.currect_option = currect_option;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getSemester_name() {
        return semester_name;
    }

    public void setSemester_name(String semester_name) {
        this.semester_name = semester_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getQuiz_num() {
        return quiz_num;
    }

    public void setQuiz_num(String quiz_num) {
        this.quiz_num = quiz_num;
    }

    public String getMcqs_no() {
        return mcqs_no;
    }

    public void setMcqs_no(String mcqs_no) {
        this.mcqs_no = mcqs_no;
    }

    public String getMcqs_text() {
        return mcqs_text;
    }

    public void setMcqs_text(String mcqs_text) {
        this.mcqs_text = mcqs_text;
    }

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    public String getCurrect_option() {
        return currect_option;
    }

    public void setCurrect_option(String currect_option) {
        this.currect_option = currect_option;
    }

    public void setCorrect_option(String correct_option) {
        this.correct_option = correct_option;
    }

    public String getCorrect_option() {
        return correct_option;
    }
}
