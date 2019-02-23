package com.dabinu.app.electroniclogbook.models;

import java.util.ArrayList;

/**
 * Created by Taslim Oseni on 2/21/19.
 */

public class MyStudents {

    public ArrayList<String> mystudents;

    public MyStudents(){

    }

    public MyStudents(ArrayList<String> mystudents){
        this.mystudents = mystudents;
    }

    public ArrayList<String> getMystudents(){
        return mystudents;
    }
}