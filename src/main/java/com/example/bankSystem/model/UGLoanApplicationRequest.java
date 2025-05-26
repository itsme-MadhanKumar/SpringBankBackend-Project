package com.example.bankSystem.model;

public class UGLoanApplicationRequest
{
    private String userId;
    private String name;
    private String age;
    private String dob; // Format: "yyyy-MM-dd"
    private String tenthPercentage;
    private String twelfthPercentage;
    private String universityName;
    private String courseName;
    private String courseDuration;
    private String counselingCode;
    private String aadhaarCard;
    private String panCard;
    private String fatherIncome;
    private String loanAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTenthPercentage() {
        return tenthPercentage;
    }

    public void setTenthPercentage(String tenthPercentage) {
        this.tenthPercentage = tenthPercentage;
    }

    public String getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public void setTwelfthPercentage(String twelfthPercentage) {
        this.twelfthPercentage = twelfthPercentage;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCounselingCode() {
        return counselingCode;
    }

    public void setCounselingCode(String counselingCode) {
        this.counselingCode = counselingCode;
    }

    public String getAadhaarCard() {
        return aadhaarCard;
    }

    public void setAadhaarCard(String aadhaarCard) {
        this.aadhaarCard = aadhaarCard;
    }

    public String getPanCard() {
        return panCard;
    }

    public void setPanCard(String panCard) {
        this.panCard = panCard;
    }

    public String getFatherIncome() {
        return fatherIncome;
    }

    public void setFatherIncome(String fatherIncome) {
        this.fatherIncome = fatherIncome;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }
}