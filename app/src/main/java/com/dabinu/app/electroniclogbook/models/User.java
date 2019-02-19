package com.dabinu.app.electroniclogbook.models;

/**
 * Created by Taslim Oseni on 2/17/19.
 */
public class User{

    public String type, fullname, email, matric, level, faculty, department, staffID, phone, rank, filledPlacement, photo_url;

    public User(){

    }


    public User(String type, String fullname, String email, String matric, String level, String faculty, String department, String filledPlacement, String photo_url){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.matric = matric;
        this.level = level;
        this.faculty = faculty;
        this.department = department;
        this.filledPlacement = filledPlacement;
        this.photo_url = photo_url;
    }

    public User(String type, String fullname, String email, String faculty, String department, String staffID, String photo_url){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.faculty = faculty;
        this.department = department;
        this.staffID = staffID;
        this.photo_url = photo_url;
    }

    public User(String type, String fullname, String email, String phone, String rank, String staffId, String plusOne, String extraOne, String extraTwo, String photo_url){
        this.type = type;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.rank = rank;
        this.staffID = staffId;
        this.photo_url = photo_url;
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

    public String getStaffID(){
        return staffID;
    }

    public String getPhone(){
        return phone;
    }

    public String getRank(){
        return rank;
    }

    public String getFilledPlacement(){
        return filledPlacement;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}