package com.example.iquiz;

public class StudentsMarksModel {

    public String student_id;
    public String test_marks;

    public String section,department,semester;

    public StudentsMarksModel() {

    }

    public StudentsMarksModel(String student_id, String test_marks, String section, String department, String semester) {
        this.student_id = student_id;
        this.test_marks = test_marks;
        this.section = section;
        this.department = department;
        this.semester = semester;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
