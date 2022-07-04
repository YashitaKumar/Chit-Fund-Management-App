package com.example.chitfund;

import java.io.Serializable;

public class User implements Serializable {
    private String fName;
    private String lName;
    private String phoneNumber;
    private String emailId;
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }



    User(){}

    User(String fName,String lName,String phoneNumber,String emailId){
        this.fName=fName;
        this.lName=lName;
        this.phoneNumber=phoneNumber;
        this.emailId=emailId;
    }
}
