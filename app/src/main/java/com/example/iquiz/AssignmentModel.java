package com.example.iquiz;

public class AssignmentModel {

    String assignName;
    String assignPath;
    String studentID;

    public AssignmentModel()
    {

    }

    public AssignmentModel(String assignName, String assignPath, String studentID) {
        this.assignName = assignName;
        this.assignPath = assignPath;
        this.studentID = studentID;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public void setAssignPath(String assignPath) {
        this.assignPath = assignPath;
    }

    public String getAssignName() {
        return assignName;
    }

    public String getAssignPath() {
        return assignPath;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
}
