package com.dabinu.app.electroniclogbook.models;

/**
 * Created by Taslim Oseni on 2/19/19.
 */
public class PlacementObject{

    public String companyName, companyAddress, companyEmail, supervisorName, supervisorEmail, numberOfWeeks;

    public PlacementObject(){

    }

    public PlacementObject(String companyName, String companyAddress, String companyEmail, String supervisorName, String supervisorEmail, String numberOfWeeks){
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyEmail = companyEmail;
        this.supervisorName = supervisorName;
        this.supervisorEmail = supervisorEmail;
        this.numberOfWeeks = numberOfWeeks;
    }


    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public String getSupervisorName() {
        return supervisorName;
    }

    public String getSupervisorEmail() {
        return supervisorEmail;
    }

    public String getNumberOfWeeks() {
        return numberOfWeeks;
    }

}