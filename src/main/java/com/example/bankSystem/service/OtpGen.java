package com.example.bankSystem.service;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpGen
{
    private static final SecureRandom random = new SecureRandom();
    private static final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String email)
    {
        String otp = String.valueOf(100000 + random.nextInt(900000));
        otpStorage.put(email, otp); // Store OTP in a map
        return otp;
    }
    public String getStoredOtp(String email)
    {  try
       {
        return otpStorage.get(email);
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
       }
        return "null";
    }
    public void removeStoredOtp(String email)
    {
        otpStorage.remove(email);
    }
}
