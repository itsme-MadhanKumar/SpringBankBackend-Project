package com.example.bankSystem.service;
import com.example.bankSystem.model.User;
import com.example.bankSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LoginService
{
    @Autowired
    private UserRepo repo;
    //----------Verify credentials
    public String verifyCrediential(User user)
    {
        String email = user.getEmail();
        if(!email.contains("@gmail.com"))
        {
            return "email";
        }
        else
            return "success";
    }

    //----------sending register data to repo
    public void senddetailsToDB(String email,HashMap<String,User> temp)
    {
        repo.addNewUser(email,temp);
    }

    //---------User already exist ?
    public boolean alreadyExistservice(String aadhar)
    {
        return repo.isUserAlreadyExist(aadhar);
    }

    //---------access to user
    public boolean userAccessService(String aadhar, String password)
    {
           return repo.verifyPassword(aadhar,password);
    }


    public boolean userPayemntPasswordServie(String aadhar, String password)
    {
        return repo.verifyPaymentPassword(aadhar,password);
    }

    //-------status of user
    public String getStatusService(String aadhar)
    {
        return repo.getUserStatus(aadhar);
    }
}
