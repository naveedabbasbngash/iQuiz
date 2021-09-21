package com.example.iquiz;

public class TeacherResultModel {

    String student_id,test_marks,name,sub;

    public TeacherResultModel()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public TeacherResultModel(String student_id, String test_marks) {
        this.student_id = student_id;
        this.test_marks = test_marks;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getTest_marks() {
        return test_marks;
    }

    public void setTest_marks(String test_marks) {
        this.test_marks = test_marks;
    }
}
