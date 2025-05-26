package com.example.bankSystem.model;

public class OtpVerificationRequestSendPayment
{
    private String userId;
    private String otp;
    private String amount;
    private String receiverAadhar;

    public String getReceiverAadhar() {
        return receiverAadhar;
    }

    public void setReceiverAadhar(String receiverAadhar) {
        this.receiverAadhar = receiverAadhar;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
