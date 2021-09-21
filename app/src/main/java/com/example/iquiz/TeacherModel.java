package com.example.iquiz;

public class TeacherModel {
    String name,username,password,contact;

    public TeacherModel() {
    }

    public TeacherModel(String name, String username, String password, String contact) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.contact = contact;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
