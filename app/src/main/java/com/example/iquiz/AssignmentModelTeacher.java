package com.example.iquiz;

public class AssignmentModelTeacher {

    String assignName;
    String assignPath;

    public AssignmentModelTeacher()
    {

    }

    public AssignmentModelTeacher(String assignName, String assignPath) {
        this.assignName = assignName;
        this.assignPath = assignPath;
    }

    public String getAssignName() {
        return assignName;
    }

    public void setAssignName(String assignName) {
        this.assignName = assignName;
    }

    public String getAssignPath() {
        return assignPath;
    }

    public void setAssignPath(String assignPath) {
        this.assignPath = assignPath;
    }
}
