package com.example.iquiz;

public class StudentModel {

    String name,username,password,section,semester,department;
    int roll,reg;

    public StudentModel()
    {

    }

    public StudentModel(String name, String username, String password, String section, String semester, String department, int roll, int reg) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.section = section;
        this.semester = semester;
        this.department = department;
        this.roll = roll;
        this.reg = reg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getReg() {
        return reg;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }
}
