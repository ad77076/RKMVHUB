package com.example.rkmvimu.mails;

public class Vmail
{
    String from_num="";
    String sent="";
    String from="";
    String to="";
    String subject="";
    String mail="";
    String time="";
    String read="";

    public Vmail(){}
    public Vmail(String from,String from_num,String to,String subject,String mail,String time,String read,String sent)
    {
        this.from_num=from_num;
        this.sent=sent;
        this.from=from;
        this.to=to;
        this.subject=subject;
        this.mail=mail;
        this.time=time;
        this.read=read;
    }

    public String getFrom_num(){ return from_num; }
    public String getSent(){ return sent; }
    public String getFrom(){ return from; }
    public String getTo(){ return to; }
    public String getSubject(){ return subject; }
    public String getMail(){ return mail; }
    public String getTime(){ return time; }
    public String getRead(){ return read; }
}
