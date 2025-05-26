package com.example.bankSystem.model;

public class SendPaymentRequest
{
    private String receiverAadhar;
    private String receiverPan;
    private String loginPassword;
    private String paymentPassword;
    private String amount;
    private String userId;


    public String getReceiverAadhar() {
        return receiverAadhar;
    }

    public void setReceiverAadhar(String receiverAadhar) {
        this.receiverAadhar = receiverAadhar;
    }

    public String getReceiverPan() {
        return receiverPan;
    }

    public void setReceiverPan(String receiverPan) {
        this.receiverPan = receiverPan;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
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
}
