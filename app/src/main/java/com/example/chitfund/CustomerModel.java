package com.example.chitfund;

public class CustomerModel {

    String fname,lname,email,image,UID;
    long mobile;
    int slab;

    CustomerModel()
    {

    }
    public CustomerModel(String fname, String lname, String email, String image, String UID, long mobile, int slab) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.image = image;
        this.mobile = mobile;
        this.slab = slab;
        this.UID = UID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobile() {
        return String.valueOf(mobile);
    }

    public void setMobile(String mobile) {
        this.mobile = Long.parseLong(mobile);
    }

    public String getSlab() {
        return String.valueOf(slab);
    }

    public void setSlab(String slab) {
        this.slab = Integer.parseInt(slab);
    }
    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
