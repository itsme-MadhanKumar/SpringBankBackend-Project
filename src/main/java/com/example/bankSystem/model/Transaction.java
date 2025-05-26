package com.example.bankSystem.model;

public class Transaction
{
    private String utrId;
    private String senderId;
    private String receiverId;
    private String credited;
    private String debited;
    private String timeStamp;
    private String paymentStatus;
    private String bankBalance;


    public String getUtrId() {
        return utrId;
    }

    public void setUtrId(String utrId) {
        this.utrId = utrId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCredited() {
        return credited;
    }

    public void setCredited(String credited) {
        this.credited = credited;
    }

    public String getDebited() {
        return debited;
    }

    public void setDebited(String debited) {
        this.debited = debited;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(String bankBalance) {
        this.bankBalance = bankBalance;
    }
}
