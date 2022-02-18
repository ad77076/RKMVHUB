package com.example.rkmvimu.data;

public class BasicInformationObject
{
    public String number="";
    public String name="";
    public String password="";
    public String email="";
    public String dept="";
    public String role="";
    public String online="";

    public BasicInformationObject(String number,String name,String password,String email,String dept,String role)
    {
        this.number=number;
        this.name=name;
        this.password=password;
        this.email=email;
        this.dept=dept;
        this.role=role;
    }
    public BasicInformationObject() {}

    public String getNumber()
    {
        return number;
    }
    public String getName()
    {
        return name;
    }
    public String getPassword()
    {
        return password;
    }
    public String getEmail()
    {
        return email;
    }
    public String getDept()
    {
        return dept;
    }
    public String getRole()
    {
        return role;
    }
    public String isOnline(){ return online; }
}
