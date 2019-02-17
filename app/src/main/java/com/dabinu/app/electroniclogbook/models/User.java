package com.dabinu.app.electroniclogbook.models;

/**
 * Created by Taslim Oseni on 2/17/19.
 */
public class User{

    public String type, fullname, email, matric, level, faculty, department, staffID, phone, rank;

    public User(){

    }


    public User(String type, String fullname, String email, String matric, String level, String faculty, String department){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.matric = matric;
        this.level = level;
        this.faculty = faculty;
        this.department = department;
    }

    public User(String type, String fullname, String email, String faculty, String department, String staffID){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
        this.staffID = staffID;
    }

    public User(String type, String fullname, String email, String phone, String rank, String staffId, String plusOne, String extraOne){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.rank = rank;
        this.staffID = staffId;
    }


    public String getType() {
        return type;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getMatric() {
        return matric;
    }

    public String getLevel() {
        return level;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getDepartment() {
        return department;
    }
}