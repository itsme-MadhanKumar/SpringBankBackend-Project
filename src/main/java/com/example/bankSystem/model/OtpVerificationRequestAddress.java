package com.example.bankSystem.model;

public class OtpVerificationRequestAddress
{
    private String addresshome;
    private String otp;
    private String userId;


    public String getAddresshome() {
        return addresshome;
    }

    public void setAddresshome(String addresshome) {
        this.addresshome = addresshome;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
