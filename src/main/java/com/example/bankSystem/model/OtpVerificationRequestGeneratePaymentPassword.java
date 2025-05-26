package com.example.bankSystem.model;

public class OtpVerificationRequestGeneratePaymentPassword
{
    private String userId;
    private String paymentPassword;
    private String otpofpaymentpassword;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public String getOtpofpaymentpassword() {
        return otpofpaymentpassword;
    }

    public void setOtpofpaymentpassword(String otpofpaymentpassword) {
        this.otpofpaymentpassword = otpofpaymentpassword;
    }
}
