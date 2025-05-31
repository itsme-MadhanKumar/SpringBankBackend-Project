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

    @Autowired
    private UserService userService;

    //----------Verify credentials
    public String verifyCrediential(User user)
    {
        String email = user.getEmail();
        String checkMobile =  CredentailMobile(user.getMobile());
        String checkAadhar = CredentailAadhar(user.getAadhar());
        String checkPan =  CredentaislPan(user.getPan());
        String checkPassword = userService.checkCredentailsSerive(user.getPassword());
        if(!checkMobile.equals("success")) return checkMobile;
        if(!checkAadhar.equals("success")) return checkAadhar;
        if(!checkPan.equals("success")) return checkPan;
        if(!email.contains("@gmail.com")) return "Invalid Email";
        if(!checkPassword.equals("Strong")) return checkPassword;

        return "Done";
    }
    public String CredentaislPan(String pan)
    {
         String PAN_PATTERN = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
         if(pan.matches(PAN_PATTERN))
         {
             return "success";
         }
         return "Pan Num Invalid eg: ABCDE1234F";
    }

    public String CredentailMobile(String mobile)
    {
        char[] arr = mobile.toCharArray();
        int count  = 0;
        for(int i =0;i<arr.length;i++)
        {
            if(Character.isDigit(arr[i]))
            {
                count++;
            }
        }
        if(count!=10)
        {
            return "Mobile Number must be 10 digit";
        }
        return "success";
    }

    public String CredentailAadhar(String aadhar)
    {
        char[] arr = aadhar.toCharArray();
        int count  = 0;
        for(int i =0;i<arr.length;i++)
        {
            if(Character.isDigit(arr[i]))
            {
                count++;
            }
        }
        if(count!=12)
        {
            return "Aadhar Number must be 12 digit";
        }
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
