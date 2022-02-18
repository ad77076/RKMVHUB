package com.example.rkmvimu;

public class MessageBody
{
    String user="";
    String dept="";
    String num="";
    String role="";
    String email="";
    String online="";
    public MessageBody() { }

    public MessageBody(String user,String num,String dept,String role,String email,String online)
    {
        this.dept=dept;
        this.num=num;
        this.user=user;
        this.online=online;
        this.role=role;
        this.email=email;
    }

    public String getUser(){ return user; }

    public String isOnline(){ return online; }

    public String getDept(){ return dept; }

    public String getNum(){ return num; }

    public String getRole(){ return role; }

    public String getEmail(){ return email; }

}
