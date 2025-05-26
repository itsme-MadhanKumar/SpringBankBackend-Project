package com.example.bankSystem.controller;
import com.example.bankSystem.model.*;
import com.example.bankSystem.service.MailSender;
import com.example.bankSystem.service.OtpGen;
import com.example.bankSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
@RestController
@CrossOrigin(origins = "*")
public class PaymentController
{
    @Autowired
    private UserService service;
    @Autowired
    private OtpGen otpGen;
    @Autowired
    private MailSender mailSender;

    @PostMapping("/api/user/add-money-request")
    public ResponseEntity<?> updatePassword(@RequestBody AddPaymentRequest request)
    {
        boolean isGeneratedPaymentPassword = service.isPaymentPasswordGeneratedService(request.getUserId());
        if(!isGeneratedPaymentPassword)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Generate Payment Password"));
        }
        else
        {
            // ---- need to check all credientials
            String flag = service.checkCredentialsOfAddMoneyService(request);
            if(!flag.equals("done"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", flag));
            }
        }
        String otp = otpGen.generateOtp(request.getGmail());
        mailSender.sending(request.getGmail(),otp);
        request.clearSensitiveData();
        return ResponseEntity.ok(Collections.singletonMap("success", true));
    }


    @PostMapping("api/verify-AddPayment-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerificationRequestAddPayment request) {
        Map<String, Object> response = new HashMap<>();
        String Generate_TransactionId = service.generateTransactionId();
        String email = service.fetchingEmailService(request.getUserId());
        String storedOtp = otpGen.getStoredOtp(email);
        String temp = "";
        long CurrentBalance_temp = 0;
        if(storedOtp.equals(request.getOtp()))
        {
            //----amount to credit in account
            String CurrentBalance = service.CurrentBalanceService(request.getUserId());
            String AddingMoney = request.getAmount();
            long add = Long.parseLong(CurrentBalance) + Long.parseLong(AddingMoney);
            CurrentBalance_temp = add;
            boolean isUpdatedinDatabase = service.UpdatingCurrentBalanceService(request.getUserId(),String.valueOf(add));
            if(isUpdatedinDatabase)
            {
                //-----Notify to the user
                String last4 = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
                temp = last4;
                String msg = "₹" + request.getAmount() + " added successfully to User ID: " + request.getUserId() + " (Txn ID: xxxx" + last4 + ")";
                service.updatingFailedPaymentNotificationService(request.getUserId(), "Payment Completed", msg);
                service.UpdateTransactionService(Generate_TransactionId,request.getUserId(),request.getUserId(),request.getAmount(),"Completed",String.valueOf(add));
                try{Thread.sleep(8000);} catch (InterruptedException e) {System.out.println("Thread interrupted: " + e.getMessage());}
                response.put("success", true);
                response.put("message", "Something went wrong in Add Payment");
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Payment Not Completed");
            return ResponseEntity.badRequest().body(response);
        }
        String last4 = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
        String msg = "₹" + request.getAmount() + " was Failed  to  add User ID: " + request.getUserId() + " (Txn ID: xxxx" + last4 + ")";
        service.updatingFailedPaymentNotificationService(request.getUserId(), "Payment Failed", msg);
        service.UpdateFailedTransactionService(Generate_TransactionId,request.getUserId(),request.getUserId(),request.getAmount(),"Failed",String.valueOf(CurrentBalance_temp));
        try{Thread.sleep(8000);} catch (InterruptedException e) {System.out.println("Thread interrupted: " + e.getMessage());}
        response.put("success", false);
        response.put("message", "Invalid OTP. Please try again.");
        return ResponseEntity.badRequest().body(response);
    }



    @PostMapping("/api/send-money-request")
    public ResponseEntity<?> SendPaymenttoUser(@RequestBody SendPaymentRequest request)
    {
        boolean isUserActive = service.currentStatusofUser(request.getReceiverAadhar());
        if(!isUserActive)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Receiver Account is in Freeze"));
        }
        boolean isGeneratedPaymentPassword = service.isPaymentPasswordGeneratedService(request.getUserId());
        if(!isGeneratedPaymentPassword)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Generate Payment Password"));
        }
        else
        {
            // ---- need to check all credientials
            String flag = service.validateSendMoneyCredentialsService(request);
            if(!flag.equals("checked"))
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", flag));
            }
        }
        String email = service.fetchingEmailService(request.getUserId());
        String otp = otpGen.generateOtp(email);
        System.out.println("Gamil : "+email);
//        mailSender.sending(email,otp);
        System.out.println("OTP "+otp);
        return ResponseEntity.ok(Collections.singletonMap("success", true));

    }

    @PostMapping("api/verify-SendPayment-otp")
    public ResponseEntity<Map<String, Object>> verifyOtpofSendMoney(@RequestBody OtpVerificationRequestSendPayment request) {
        Map<String, Object> response = new HashMap<>();
        String Generate_TransactionId = service.generateTransactionId();
        String email = service.fetchingEmailService(request.getUserId());
        String storedOtp = otpGen.getStoredOtp(email);
        System.out.println();
        if(storedOtp.equals(request.getOtp()))
        {
//           // Amount to be debited from current account
            String CurrentBalance1 = service.CurrentBalanceService(request.getUserId());
            String AddingMoney1 = request.getAmount();
            long add1 = Long.parseLong(CurrentBalance1) - Long.parseLong(AddingMoney1);
            boolean isUpdatedinDatabase1 = service.UpdatingCurrentBalanceService(request.getUserId(),String.valueOf(add1));
//
//          ----amount to credit in account
            String CurrentBalance = service.CurrentBalanceService(request.getReceiverAadhar());
            String AddingMoney = request.getAmount();
            long add = Long.parseLong(CurrentBalance) + Long.parseLong(AddingMoney);
            boolean isUpdatedinDatabase = service.UpdatingCurrentBalanceService(request.getReceiverAadhar(),String.valueOf(add));
            if(isUpdatedinDatabase && isUpdatedinDatabase1)
            {
                String last = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
                String msg1 = "₹" + request.getAmount() + " has  been credited to your Account from User ID : " + request.getReceiverAadhar() + " (Txn ID: xxxx" + last + ")";
                String msg2 = "₹" + request.getAmount() + " has been sent to User ID : " + request.getUserId() + " (Txn ID: xxxx" + last + ")";
//                //-----Notify to the user

                service.updatingFailedPaymentNotificationService(request.getReceiverAadhar(), "Amount Credited",msg1);
                service.updatingFailedPaymentNotificationService(request.getUserId(), "Amount Debited",msg2);
//                -----------------------------Update Transaction history
                service.UpdateTransactionofSendMoneyService(Generate_TransactionId,request.getUserId(),request.getReceiverAadhar(),request.getAmount(),"Completed",String.valueOf(add));
                try{Thread.sleep(8000);} catch (InterruptedException e) {System.out.println("Thread interrupted: " + e.getMessage());}
                response.put("success", true);
                response.put("message", "Password Changed Successfully - You Have been Logged out!!!");
                return ResponseEntity.ok(response);
            }
            response.put("success", false);
            response.put("message", "Payment Not Updated");
            return ResponseEntity.badRequest().body(response);
        }
        response.put("success", false);
        response.put("message", "Invalid OTP. Please try again.");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("api/loan/search/{applicationId}")
    public ResponseEntity<?> getLoanDetails(@PathVariable String applicationId)
    {
        LoanDetails loanUg = service.findLoanByApplicationId(applicationId);
        LoanDetails loanPg = service.findPgLoanByApplicationId(applicationId);
        LoanDetails loanAbroad = service.findAboadLoanByApplicationId(applicationId);
        if (loanUg != null)
        {
            return ResponseEntity.ok(Map.of(
                    "found", true,
                    "application_id", loanUg.getApplicationId(),
                    "name", loanUg.getName(),
                    "aadhaar_card", loanUg.getAadhaarCard(),
                    "loan_amount", loanUg.getLoanAmount()
            ));
        }
        else if(loanPg !=null)
        {
            return ResponseEntity.ok(Map.of(
                    "found", true,
                    "application_id", loanPg.getApplicationId(),
                    "name", loanPg.getName(),
                    "aadhaar_card", loanPg.getAadhaarCard(),
                    "loan_amount", loanPg.getLoanAmount()
            ));
        }
        else if(loanAbroad !=null)
        {
            return ResponseEntity.ok(Map.of(
                    "found", true,
                    "application_id", loanAbroad.getApplicationId(),
                    "name", loanAbroad.getName(),
                    "aadhaar_card", loanAbroad.getAadhaarCard(),
                    "loan_amount", loanAbroad.getLoanAmount()
            ));
        }
        else
        {
            return ResponseEntity.ok(Map.of("found", false));
        }
    }


    @PostMapping("api/loan/pay")
    public ResponseEntity<?> payLoan(@RequestBody Map<String, String> payload)
    {
        String applicationId = payload.get("applicationId");
        String amount = payload.get("amount");
        String userId = payload.get("userId");
        if(amount.isEmpty())
        {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid Money Format"));
        }
        String currentBalanceofUser = service.CurrentBalanceService(userId);//99500
        String checkMoneyCredentials = service.checkMoney(amount); // done
        String Loan_SanchendMoney = service.gettingLoanSancnedMoneyService(applicationId);//1000
        if(amount.matches(".*\\d+\\.\\d+.*"))
        {
            return ResponseEntity.ok(Map.of("success", false, "message", "Decimal Values are Not Accepted."));
        }
        long amountToPay = Long.parseLong(amount);//1000
        long currentBalance = Long.parseLong(currentBalanceofUser);//99500
        long loan_amount = Long.parseLong(Loan_SanchendMoney);//1000
        if(!checkMoneyCredentials.equals("checked"))
        {
            return ResponseEntity.ok(Map.of("success", false, "message", checkMoneyCredentials));
        }
        else if(amountToPay>currentBalance)
        {
            return ResponseEntity.ok(Map.of("success", false, "message", "Insufficient balance. You cannot pay more than your available balance."));
        }
        else if(loan_amount<amountToPay)
        {
            return ResponseEntity.ok(Map.of("success", false, "message", "Your Payable Amount is more than the Loan Amount"));
        }
        String email = service.fetchingEmailService(userId);
        String otp = otpGen.generateOtp(email);
        System.out.println("OTP loan pay "+otp);
//        mailSender.sending(email,otp);
        return ResponseEntity.ok(Map.of("success", true));
    }


    @PostMapping("api/loan/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerificationRequestPayLoan request)
    {
        Map<String, Object> response = new HashMap<>();
        String email = service.fetchingEmailService(request.getUserId());
        String storedOtp = otpGen.getStoredOtp(email);
        if(!storedOtp.equals(request.getOtp()))
        {
            response.put("success", false);
            response.put("message", "Invalid OTP");
            return ResponseEntity.badRequest().body(response);
        }
        else
        {
            long fetchingActualLoan = Long.parseLong(service.CurrentActualLoanBalanceService(request.getApplicationId()));
            if(fetchingActualLoan==-1)
            {
                response.put("success", false);
                response.put("message", "Something went wrong !!! Try Later");
                return ResponseEntity.badRequest().body(response);
            }
            if(fetchingActualLoan==0)
            {
                response.put("success", false);
                response.put("message", "You have already Pain this Loan");
                return ResponseEntity.badRequest().body(response);
            }
            if((request.getPayAmount()).matches(".*\\d+\\.\\d+.*"))
            {
                response.put("success", false);
                response.put("message", "Decimal Values are Not Accepted");
                return ResponseEntity.badRequest().body(response);
            }
            String Generate_TransactionId = service.generateTransactionId();
            long needToDeduct = Long.parseLong(request.getPayAmount());
            long bank_balance = Long.parseLong(service.CurrentBalanceService(request.getUserId()));
            long sub = bank_balance-needToDeduct;
            boolean DeductingMoneyfromuser = service.UpdatingCurrentBalanceService(request.getUserId(),String.valueOf(sub));
            long sub2 = fetchingActualLoan-needToDeduct;
            String userIdofCurrentUserByAppID = service.gettingUserIDusingAppID(request.getApplicationId());
            boolean UpdatingbankTable = service.deductingMoneyPayLoanService(userIdofCurrentUserByAppID,request.getApplicationId(),String.valueOf(sub2));
            if(DeductingMoneyfromuser && UpdatingbankTable)
            {
                //---------
                service.UpdateTransactionofSendMoneyService(Generate_TransactionId,request.getUserId(),"Bank",request.getPayAmount(),"Loan Payment Completed",request.getPayAmount());
                service.updatingFailedPaymentNotificationService(request.getUserId(), "Loan Amount Received Successfully", "Dear user , \n Rs :"+needToDeduct+" has be Deducted from your bank account for re-pay of Educational Loan -Thank you!");
            }
            if(!request.getUserId().equals(userIdofCurrentUserByAppID))
            {
                service.updatingFailedPaymentNotificationService(userIdofCurrentUserByAppID, "Loan Amount Received Successfully", "Dear user , Your Loan Amount \n Rs :"+needToDeduct+" has be Paid by User ID : "+request.getUserId()+ " -Thank you!");
            }
            long making0inActualLoanifPossible = Long.parseLong(service.CurrentActualLoanBalanceService(request.getApplicationId()));
            if(making0inActualLoanifPossible==0)
            {
                service.setting_0_Service(userIdofCurrentUserByAppID,request.getApplicationId(),"Loan Closed");
            }
            else
            {
                service.setting_0_Service(userIdofCurrentUserByAppID, request.getApplicationId(),"Repayment Started");
            }
        }
        try{Thread.sleep(8000);} catch (InterruptedException e) {System.out.println("Thread interrupted: " + e.getMessage());}
        response.put("success", true);
            response.put("message", "OTP verified successfully.");
        return ResponseEntity.ok(response);
    }
}
