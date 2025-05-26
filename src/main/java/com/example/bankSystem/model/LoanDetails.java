package com.example.bankSystem.model;

public class LoanDetails
{

    private String applicationId;
    private String name;
    private String aadhaarCard;
    private String loanAmount;

    public LoanDetails() {}

    public LoanDetails(String applicationId, String name, String aadhaarCard, String loanAmount) {
        this.applicationId = applicationId;
        this.name = name;
        this.aadhaarCard = aadhaarCard;
        this.loanAmount = loanAmount;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAadhaarCard() {
        return aadhaarCard;
    }

    public void setAadhaarCard(String aadhaarCard) {
        this.aadhaarCard = aadhaarCard;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }
}
