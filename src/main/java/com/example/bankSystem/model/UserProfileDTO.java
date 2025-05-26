package com.example.bankSystem.model;

public class UserProfileDTO
{
    private String fullname;
    private String profilePicBase64; // send image as base64 string


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }

    public void setProfilePicBase64(String profilePicBase64) {
        this.profilePicBase64 = profilePicBase64;
    }
}
