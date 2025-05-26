package com.example.bankSystem.model;

public class AddPaymentRequest
{
    private String aadhar;
    private String pan;
    private String gmail;
    private String paymentPassword;
    private String amount;
    private String userId;

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

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }


    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void clearSensitiveData() {
        this.aadhar = null;
        this.pan = null;
        this.gmail = null;
        this.paymentPassword = null;
        this.amount = null;
        this.userId = null;
    }

}
