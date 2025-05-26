package com.example.bankSystem.controller;
import com.example.bankSystem.model.*;
import com.example.bankSystem.repository.UserRepo;
import com.example.bankSystem.service.LoginService;
import com.example.bankSystem.service.MailSender;
import com.example.bankSystem.service.OtpGen;
import com.example.bankSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
public class AdminController
{
    @Autowired
    private UserService service;
    @Autowired
    UserRepo repo;
    @Autowired
    private OtpGen otpGen;
    @Autowired
    private MailSender mailSender;

    @Autowired
    private LoginService loginService;



    @PostMapping("/api/user/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getUserId());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Account is in Freeze"));
        }
        boolean flah = service.CheckOldPasswordService(request.getUserId(),request.getOldPassword());
        if (flah)
        {
            String email = service.fetchingEmailService(request.getUserId());
            String checkCredential = service.checkCredentailsSerive(request.getNewPassword());
            if(email.equals("not found"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Email was Not found"));
            }
            if(!checkCredential.equals("Strong"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", checkCredential));
            }
            String otp = otpGen.generateOtp(email);
            mailSender.sending(email,otp);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Old password is incorrect"));
        }
    }


    @PostMapping("api/verify-otp-pop")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerificationRequestPassword request) {
        Map<String, Object> response = new HashMap<>();
        String email = service.fetchingEmailService(request.getUserId());
        String storedotp = otpGen.getStoredOtp(email);
        System.out.println("Stored otp = "+request.getOtp()+" Gen OTP = "+storedotp);
        if (storedotp.equals(request.getOtp()))
        {
            otpGen.removeStoredOtp(email);
            service.updatingUserPasswordService(request.getUserId(),request.getNewPassword());
            String msg = "Hello, your password has been changed successfully. If this wasn't you, please contact customer support immediately.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Password Change Successful", msg);
            response.put("success", true);
            response.put("message", "Password Changed Successfully - You Have been Logged out!!!");
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("success", false);
            response.put("message", "Invalid OTP. Please try again.");
            String msg = "Hello, your password change attempt has failed due to a system error or incorrect credentials. Please try again or contact customer support if the issue persists.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Password Change Failed", msg);
            otpGen.removeStoredOtp(email);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/api/user/update-mobile")
    public ResponseEntity<?> updateMobile(@RequestBody MobileUpdateRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getUserId());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Account is in Freeze"));
        }
        String flah = service.fetchingOldMobileService(request.getUserId(),request.getOldMobile());
        if (flah.equals(request.getOldMobile()))
        {
            String email = service.fetchingEmailService(request.getUserId());
            String checkMobileNumberCredential = service.checkMobileCredentailService(request.getNewMobile());
            if(email.equals("not found"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Email was Not found"));
            }
            if(!checkMobileNumberCredential.equals("Verified"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", checkMobileNumberCredential));
            }
            String otp = otpGen.generateOtp(email);
            mailSender.sending(email,otp);
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Old Mobile Number is incorrect"));
        }
    }

    @PostMapping("api/verify-otp-pop-mobile")
    public ResponseEntity<Map<String, Object>> verifyOtpMobile(@RequestBody OtpVerificationRequestMobile request)
    {
        Map<String, Object> response = new HashMap<>();
        String email = service.fetchingEmailService(request.getUserId());
        String storedotp = otpGen.getStoredOtp(email);
        System.out.println("New mobile"+request.getNewMobile());
        if (storedotp.equals(request.getOtp()))
        {
            service.updatingUserMobileService(request.getUserId(),request.getNewMobile());
            otpGen.removeStoredOtp(email);
            String msg = "Hello, your mobile number has been updated successfully in our system. If this wasn't you, please contact customer support immediately.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Mobile Number Change Successful", msg);
            response.put("success", true);
            response.put("message", "Mobile Number Changed Successfully");
            return ResponseEntity.ok(response);
        } else
        {
            response.put("success", false);
            response.put("message", "Invalid OTP. Please try again.");
            String msg = "Hello, your mobile number update attempt has failed. Please ensure the details are correct and try again. If the issue continues, contact customer support.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Mobile Number Change Failed", msg);
            otpGen.removeStoredOtp(email);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("/api/email/change-email-request")
    public ResponseEntity<?> changeEmail(@RequestBody EmailUpdateRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getUserId());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Account is in Freeze"));
        }
        String storedemail = service.fetchingEmailService(request.getUserId());
        if (storedemail.equals(request.getOldEmail()))
        {
            if(storedemail.equals(request.getNewEmail()))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "New and Old email should not same"));
            }
            String checkEmailCredentails = service.checkEmailCredentialsService(request.getNewEmail());
            if(!checkEmailCredentails.equals("Verified"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", checkEmailCredentails));
            }
            String otp1Gen = otpGen.generateOtp(request.getOldEmail());
            String otp2Gen = otpGen.generateOtp(request.getNewEmail());
            mailSender.sending(request.getOldEmail(),otp1Gen);
            mailSender.sending(request.getNewEmail(),otp2Gen);
            return ResponseEntity.ok(Collections.singletonMap("success", true));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Old email is Invalid !"));
    }

    @PostMapping("api/email/verify-dual-otp")
    public ResponseEntity<Map<String, String> > verifyOtps(@RequestBody OtpVerificationRequestEmailtwo request)
    {
        Map<String, String> response = new HashMap<>();
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String NewEmail = request.getNewEmail();
        String OtpOfCurrentEmail = otpGen.getStoredOtp(Curentemail);
        String OtpOfNewEmail = otpGen.getStoredOtp(NewEmail);

        if ((OtpOfCurrentEmail.equals(request.getOldOtp()))&&(OtpOfNewEmail.equals(request.getNewOtp())))
        {
            service.updatingUserEmailService(request.getUserId(),request.getNewEmail());
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, your email address has been updated successfully in our system. If this wasn't you, please contact customer support immediately.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Email Change Successful", msg);
            response.put("message", "Email updated successfully");
            response.put("success", "true");
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("error", "Invalid OTPs");
            response.put("success", "false");
            String msg = "Hello, your email update attempt has failed. Please ensure the email is valid and try again. If the issue persists, contact customer support.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Email Change Failed", msg);
            otpGen.removeStoredOtp(Curentemail);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @PostMapping("/api/user/update-address")
    public ResponseEntity<?> updateAddress(@RequestBody AddressUpdateRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getUserId());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Account is in Freeze"));
        }
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GeneratedOtp = otpGen.generateOtp(Curentemail);
        mailSender.sending(Curentemail,GeneratedOtp);
        if (GeneratedOtp!=null && Curentemail!=null)
        {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Old password is incorrect"));
        }
    }

    @PostMapping("api/verify-otp-pop-address")
    public ResponseEntity<Map<String, Object>> verifyOtpAddress(@RequestBody OtpVerificationRequestAddress request)
    {

        Map<String, Object> response = new HashMap<>();
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GeneratedOtp = otpGen.getStoredOtp(Curentemail);
        if (GeneratedOtp.equals(request.getOtp()))
        {
            otpGen.removeStoredOtp(Curentemail);
            service.updatingHomeAddressService(request.getUserId(),request.getAddresshome());
            response.put("success", true);
            response.put("message", "Home Address Changed Successfully");
            String msg = "Hello, your address has been updated successfully in our system. If you did not request this change, please contact customer support immediately.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Address Change Successful", msg);
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("success", false);
            response.put("message", "Invalid OTP. Please try again.");
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, your address update attempt has failed. Please ensure the provided details are correct and try again. If the issue persists, contact customer support.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Address Change Failed", msg);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("api/profile/updateProfilePic")
    public ResponseEntity<String> updateProfilePic(@RequestParam("userId") String aadhar, @RequestParam("profilePic") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            boolean isUpdated = service.updateProfilePicture(aadhar,inputStream);

            if (isUpdated)
            {
                return ResponseEntity.ok("Profile picture updated successfully.");
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @GetMapping("api/user/profile/{aadhar}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String aadhar)
    {
        UserProfileDTO profile = repo.getUserProfile(aadhar);
        if (profile != null)
        {
            return ResponseEntity.ok(profile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/api/user/update-takeaction")
    public ResponseEntity<Map<String,Object>> updateTakeAction(@RequestBody AccountActionRequest request)
    {
        Map<String, Object> response = new HashMap<>();
        String currentStatus = service.currentStatusService(request.getUserId());
        if(currentStatus.equalsIgnoreCase("Active") && request.getAction().equalsIgnoreCase("Active"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "user is already Active "));
        }
        if(currentStatus.equalsIgnoreCase("Freeze") && request.getAction().equalsIgnoreCase("Freeze"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "user is already in Freeze "));
        }
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GenerateOtp = otpGen.generateOtp(Curentemail);
        mailSender.sending(Curentemail,GenerateOtp);
        if (request.getAction()!=null)
        {
            return ResponseEntity.ok(Collections.singletonMap("success", true));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "There is a Error"));
        }
    }

    @PostMapping("api/verify-otp-pop-takeAction")
    public ResponseEntity<Map<String, Object>> verifyOtpTakeAction(@RequestBody OtpVerificationRequestTakeaction request) {

        Map<String, Object> response = new HashMap<>();
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GeneratedOtp = otpGen.getStoredOtp(Curentemail);
        if (GeneratedOtp.equals(request.getOtp()))
        {
            repo.updateUserStatus(request.getUserId(),request.getAction());
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, your request to [Freeze/Deactivate/Delete] your account has been successfully processed. If this wasn't you, please contact customer support immediately.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Account Action Successful", msg);
            response.put("success", true);
            response.put("message", "Action Taken Successfully!");
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("success", false);
            response.put("message", "Invalid OTP. Please try again.");
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, your request to [Freeze/Deactivate/Delete] your account could not be completed. Please try again later or contact customer support for assistance.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Account Action Failed", msg);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("api/user/dashboard")
    public ResponseEntity<UserDashboardDTO> getUserDashboard(@RequestParam String userId)
    {
        UserDashboardDTO user = service.fetchUserDashboard(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/user/generate-payment-password")
    public ResponseEntity<?> GeneratePaymentPassword(@RequestBody GeneratePaymentPasswordRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getUserId());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Your Account is in Freeze"));
        }
        boolean passwordMatch = loginService.userAccessService(request.getUserId(),request.getGeneratedpassword());
        boolean currentPaymentPassword = loginService.userPayemntPasswordServie(request.getUserId(),request.getGeneratedpassword());
        String credentials =service.checkCredentailsSerive(request.getGeneratedpassword());
        if(currentPaymentPassword)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "New Password Should not Match with old password!"));
        }
        if(passwordMatch)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Payment Password and Login Password should not be same"));
        }
        if(!credentials.equals("Strong"))
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", credentials));
        }
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GenerateOtp = otpGen.generateOtp(Curentemail);
        mailSender.sending(Curentemail,GenerateOtp);
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }

    @PostMapping("api/verify-otp-generatePaymentPassword")
    public ResponseEntity<Map<String, Object>> verifyotpOfPaymentpassword(@RequestBody OtpVerificationRequestGeneratePaymentPassword request) {
        Map<String, Object> response = new HashMap<>();
        String Curentemail = service.fetchingEmailService(request.getUserId());
        String GeneratedOtp = otpGen.getStoredOtp(Curentemail);
        if (GeneratedOtp.equals(request.getOtpofpaymentpassword()))
        {
            service.UpdatingPaymentPasswordService(request.getUserId(),request.getPaymentPassword());
            response.put("success", true);
            response.put("message", "Payment Password changed Successfully ✅");
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, your payment password has been generated successfully. Please keep it confidential and do not share it with anyone.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Payment Password Generated", msg);
            return ResponseEntity.ok(response);
        }
        else
        {
            response.put("success", false);
            response.put("message", "Invalid OTP. Please try again.");
            otpGen.removeStoredOtp(Curentemail);
            String msg = "Hello, we were unable to generate your payment password due to a system issue or invalid request. Please try again later or contact customer support.";
            service.updatingFailedPaymentNotificationService(request.getUserId(), "Payment Password Generation Failed", msg);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("api/health-check")
    public ResponseEntity<String> checkHealth()
    {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("api/transactions/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable String userId)
    {
        return service.getTransactionsByUserId(userId);
    }

    @GetMapping("api/notifications")
    public List<NotificationDTO> getNotifications(@RequestParam String userId)
    {
        return repo.getUserNotifications(userId);
    }

    @GetMapping("api/delete-step/{step}")
    public ResponseEntity<Map<String, String>> deleteStep(@PathVariable("step") int step, @RequestParam String userId) throws SQLException {
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        String email = service.fetchingEmailService(userId);
        Map<String, String> response = new HashMap<>();
        switch (step)
        {
            case 1 -> response.put("success", "true");
            case 2 ->
            {
                boolean isPendingLoan = service.isAnyPendingLoanService(userId);
                if (!isPendingLoan) {
                    response.put("message", "You have pending loans!");
                    response.put("success", "false");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                service.deleteActionofLoanHistory(userId);
                response.put("success", "true");
            }
            case 3 -> {
                boolean flag = service.deletingTransactionService(userId);
                if (!flag) {
                    response.put("message", "Error Try Again Later");
                    response.put("success", "false");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                response.put("success", "true");
            }
            case 4 -> {
                boolean flag1 = service.deletingNotificationService(userId);
                if (!flag1) {
                    response.put("message", "Error Try Again Later");
                    response.put("success", "false");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                response.put("success", "true");
            }
            case 5 -> {
                boolean flag2 = service.deletingAccountService(userId);
                System.out.println("EMAILLLLL - " + email + " uSER IS " + userId);
                if (email == null || !email.contains("@")) {
                    response.put("message", "Email not found or invalid for user");
                    response.put("success", "false");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                mailSender.ByeByeMessage(email);
                if (!flag2) {
                    response.put("message", "Error Try Again Later");
                    response.put("success", "false");
                }
                response.put("success", "true");
            }
            default -> {
                response.put("message", "Error Try Again Later");
                response.put("success", "false");
            }
        }
        return ResponseEntity.ok(response);
    }
}
