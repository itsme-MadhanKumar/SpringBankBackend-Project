package com.example.bankSystem.controller;
import com.example.bankSystem.model.Otp;
import com.example.bankSystem.model.User;
import com.example.bankSystem.service.LoginService;
import com.example.bankSystem.service.MailSender;
import com.example.bankSystem.service.OtpGen;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LoginController
{
    @Autowired
    private LoginService service;
    @Autowired
    private OtpGen otpGen;
    @Autowired
    private MailSender mailSender;

    private final HashMap<String,User> tempStorage = new HashMap<>();
    //---------------------------------------------------------------Register form submit
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(HttpServletRequest request,@RequestBody User user)
    {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent != null && userAgent.matches(".*(Mobi|Android|iPhone|iPad|iPod).*"))
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Registering not allowed on mobile devices.\"}");
        }
        System.out.println(user.getFullname());
        System.out.println(user.getEmail());
        String flag = service.verifyCrediential(user);
        boolean isAlreadyExist = service.alreadyExistservice(user.getAadhar());
        if (!isAlreadyExist)
        {
            if (flag.equals("success"))
            {
                otpGen.generateOtp(user.getEmail());
                String generated_otp = otpGen.getStoredOtp(user.getEmail());
                mailSender.sending(user.getEmail(),generated_otp);
//                System.out.println(otpGen.getStoredOtp(user.getEmail()));
                    tempStorage.put(user.getEmail(), new User(user.getFullname(), user.getMobile(), user.getAadhar(), user.getPan(), user.getAddress(), user.getEmail(), user.getPassword()));
                    return ResponseEntity.ok("{\"message\": \"Successful Verified\"}");
            }
            return ResponseEntity.badRequest().body("{\"message\": \"Invalid Mail Address\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\": \"User Already Exist #Aadhar\"}");
    }
    //---------------------------------------------------------------OTP verification
    @PostMapping("/verify-otp")
    public ResponseEntity<?> otpOfuserLogin(@RequestBody Otp otp)
    {
        String getten_otp = otp.getOtp();
        String generated_otp = otpGen.getStoredOtp(otp.getEmail());
        if (getten_otp.equals(generated_otp))
        {
            System.out.println("OPT IS : "+otp.getOtp());
            System.out.println("Email is : "+otp.getEmail());
            service.senddetailsToDB(otp.getEmail(),tempStorage);
            return ResponseEntity.ok("{\"message\": \"verified\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\": \"Wrong Otp\"}");
    }
    //------------------------------------------------------------Login verify
    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(HttpServletRequest request, @RequestBody User user)
    {
        String userAgent = request.getHeader("User-Agent");

        if (userAgent != null && userAgent.matches(".*(Mobi|Android|iPhone|iPad|iPod).*"))
        {
            return ResponseEntity.badRequest().body("{\"message\": \"Login not allowed on mobile devices.\"}");
        }
        System.out.println(user.getAadhar());
        System.out.println(user.getPassword());
        boolean isAlreadyExist = service.alreadyExistservice(user.getAadhar());
        if(isAlreadyExist)
        {
            boolean passwordMatch = service.userAccessService(user.getAadhar(),user.getPassword());
            if(!passwordMatch)
            {
                return ResponseEntity.badRequest().body("{\"message\": \"Invalid password\"}");
            }
            String status = service.getStatusService(user.getAadhar());
            System.out.println("Status : "+status);
            if(status.equals("Pending"))
            {
                return ResponseEntity.ok("{\"message\": \"Your Access is not Approved yet! we will notify ones Approved . It may take up to 3 Minutes -Thank you\"}");
            }
            if(status.equals("Active")||status.equals("Activate"))
            {
                return ResponseEntity.ok("{\"message\": \"success\"}");
            }
            return ResponseEntity.badRequest().body("{\"message\": \"Login Failed, Try again Later !\"}");
        }
        return ResponseEntity.badRequest().body("{\"message\": \"user not registered yet!\"}");
    }
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck()
    {
        return ResponseEntity.ok("Server is running");
    }
}

