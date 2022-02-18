package com.example.rkmvimu;

public class StatusDetails {

    String statusBy="";
    String dateTime="";
    String status = "";
    String num="";

    public StatusDetails(){}

    public StatusDetails(String statusBy,String status,String number,String dateTime) {

        this.statusBy=statusBy;
        this.status = status;
        this.dateTime=dateTime;
        this.num=number;

    }

    public String getStatusBy() {
        return statusBy;
    }
    public String getDateTime() {
        return dateTime;
    }
    public String getStatus() {
        return status;
    }
    public String getNum(){ return num; }

}