package com.example.chitfund;

public class ChitModel {

    String chitid;
    String slab;
    String monthly;
    String timeline;
    String Startdate;
    String Duedate;

    ChitModel()
    {

    }

    public ChitModel(String chitid, String slab, String monthly, String timeline, String startdate, String Duedate) {
        this.chitid = chitid;
        this.slab = slab;
        this.monthly = monthly;
        this.timeline = timeline;
        this.Startdate = startdate;
        this.Duedate = Duedate;
    }

    public String getChitid() {
        return chitid;
    }

    public void setChitid(String chitid) {
        this.chitid = chitid;
    }

    public String getSlab() {
        return slab;
    }

    public void setSlab(String slab) {
        this.slab = slab;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getStartdate() {
        return Startdate;
    }

    public void setStartdate(String Startdate) {
        this.Startdate = Startdate;
    }
    public String getDuedate() {
        return Duedate;
    }

    public void setDuedate(String duedate) {
        Duedate = duedate;
    }


}
