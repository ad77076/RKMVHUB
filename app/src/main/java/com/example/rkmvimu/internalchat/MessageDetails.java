package com.example.rkmvimu.internalchat;

public class MessageDetails
{
    String flag="";
    String from="";
    String too="";
    String time="";
    String message="";

    public MessageDetails(){}

    public MessageDetails(String from,String too,String time,String message,String flag)
    {
        this.from=from;
        this.too=too;
        this.time=time;
        this.message=message;
        this.flag=flag;
    }

    public String getFrom(){ return from; }
    public String getToo(){ return too; }
    public String getTime(){ return time; }
    public String getMessage(){ return message; }
    public String getFlag(){ return flag; }

}
