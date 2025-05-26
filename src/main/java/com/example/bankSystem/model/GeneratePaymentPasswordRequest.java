package com.example.bankSystem.model;

public class GeneratePaymentPasswordRequest
{
    private String userId;
    private String generatedpassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGeneratedpassword() {
        return generatedpassword;
    }

    public void setGeneratedpassword(String generatedpassword) {
        this.generatedpassword = generatedpassword;
    }
}
