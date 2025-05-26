package com.example.bankSystem.model;
public class OtpVerificationRequestEmailtwo {
    private String userId;
    private String oldOtp;
    private String newOtp;
    private String newEmail;

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for oldOtp
    public String getOldOtp() {
        return oldOtp;
    }
    public void setOldOtp(String oldOtp) {
        this.oldOtp = oldOtp;
    }

    // Getter and Setter for newOtp
    public String getNewOtp() {
        return newOtp;
    }
    public void setNewOtp(String newOtp) {
        this.newOtp = newOtp;
    }

    // ✅ Getter and Setter for newEmail
    public String getNewEmail() {
        return newEmail;
    }
    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
