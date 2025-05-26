package com.example.bankSystem.model;
public class User
{
    private String fullname;
    private String mobile;
    private String aadhar;
    private String pan;
    private String address;
    private String email;
    private String password;
    private String gen_otp;


    public User(String name,String mob,String aadh,String pann,String addr,String email,String pass)
    {
        this.fullname = name;
        this.mobile = mob;
        this.aadhar = aadh;
        this.pan= pann;
        this.address = addr;
        this.email = email;
        this.password = pass;
    }

    public String getGen_otp()
    {
        return gen_otp;
    }

    public void setGen_otp(String gen_otp) {
        this.gen_otp = gen_otp;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
