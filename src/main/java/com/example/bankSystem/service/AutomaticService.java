package com.example.bankSystem.service;
import com.example.bankSystem.model.LoanAbroad;
import com.example.bankSystem.model.LoanPg;
import com.example.bankSystem.model.LoanUg;
import com.example.bankSystem.repository.LoanRepo;
import com.example.bankSystem.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AutomaticService
{
    @Autowired
    UserRepo repo;

    @Autowired
    LoanRepo loanRepo;

    @Autowired
    UserService userService;

    @Autowired
    LoanService loanService;

    @Autowired
    private MailSender mailSender;


//    ---------------------------------------Auto Approval of New users
    @Scheduled(fixedRate = 180000) // 3 minutes = 180,000 milliseconds
    public void AutoApprovalControllerofNewUser()
    {
        System.out.println("Auto Approval Starting...");
        List<String> confirmedAccounts = repo.AutoApprovalRepo();
        for (String a : confirmedAccounts)
        {
            userService.updatingFailedPaymentNotificationService(a, "Congratulations 🎉", "Welcome to SafeBharath Bank! Your account has been activated successfully. You can now perform transactions and apply for loans.");
            userService.updatingFailedPaymentNotificationService(a, "Notice 🔊", "Sometimes our OTP service may not work if the request volume exceeds the limit per hour. During such times, we kindly suggest trying again after an hour. Thank you for your patience.");
        }
        //- Welcome message

    }

    //    -----------------------------------Loan Auto Approval
    @Scheduled(fixedRate = 12000) // 5 minutes = 300,000 milliseconds
    public void AutoApproalofLoan()
    {
        System.out.println("Loan Approval Starting");
        List<String> pendingLoanIds = userService.gettingPendingLoanIdService();
        System.out.println(pendingLoanIds);
        for (String a : pendingLoanIds)
        {
            String userId = "";
            String app_id = loanRepo.GettingapplicationId("UG",a.replaceAll("UG",""));
            if(app_id.contains("UG")) userId = loanRepo.gettingUserIDbyAppIDrepo(app_id);
            if(a.contains("UG"))
            {
                List<String> checkUG = LoanCredentialofUG(a.replaceAll("UG",""));
                String loanAmount = userService.gettingLoanSancnedMoneyService(app_id);
                System.out.println(checkUG);
                String email = userService.fetchingEmailService(userId);
                if(checkUG.isEmpty())
                {
                    System.out.println("All are correct ------------");
                    String Generate_TransactionId = userService.generateTransactionId();
                    String last = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
                    String CurrentBalance = userService.CurrentBalanceService(userId);
                    long sum = Long.parseLong(CurrentBalance) + Long.parseLong(loanAmount);
                    boolean isUpdatedinDatabase1 = userService.UpdatingCurrentBalanceService(userId,String.valueOf(sum));
                    if(isUpdatedinDatabase1)
                    {
                        String msg = "SafeBharath Bank Transferred ₹" + loanAmount + " to your  User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Completed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Completed",String.valueOf(sum));
                    }
                    else
                    {
                        String msg = "₹" + loanAmount + "was Failed to added to User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Payment Failed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Failed",String.valueOf(sum));
                    }
                    loanRepo.UpdateSantionedLoanApplication(app_id);
                    mailSender.LoanSanctionEmailSending(email,checkUG,loanAmount);
                }
                else
                {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Bank has rejected your UG loan application. Kindly check the information before submitting. ");
                    msg.append("You can also download the sample manual for verification.\n");
                    msg.append("Errors found:\n");

                    for (String mistake : checkUG) {
                        msg.append("- ").append(mistake).append("\n");
                    }
                    loanRepo.UpdateRejectedLoanApplication(app_id);
                    String finalMessage = msg.toString();
                    userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Failed", finalMessage);
                    mailSender.LoanSanctionEmailSending(email,checkUG,loanAmount);
                }

            }
            app_id = loanRepo.GettingapplicationId("PG",a.replaceAll("PG",""));
            if(app_id.contains("PG")) userId = loanRepo.gettingUserIDbyAppIDrepo(app_id);
            if(a.contains("PG"))
            {
                //check credentails
                List<String> checkPG = LoanCredentialofPG(a.replaceAll("PG",""));
                System.out.println(checkPG);
                String email = userService.fetchingEmailService(userId);
                String loanAmount = userService.gettingLoanSancnedMoneyService(app_id);
                //update credentials(update table, Email,Notification,Money Sanction)
                if(checkPG.isEmpty())
                {
                    System.out.println("All are correct ------------");
                    String Generate_TransactionId = userService.generateTransactionId();
                    String last = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
                    String CurrentBalance = userService.CurrentBalanceService(userId);
                    long sum = Long.parseLong(CurrentBalance) + Long.parseLong(loanAmount);
                    boolean isUpdatedinDatabase1 = userService.UpdatingCurrentBalanceService(userId,String.valueOf(sum));
                    if(isUpdatedinDatabase1)
                    {

                        String msg = "SafeBharath Bank Transferred ₹" + loanAmount + " to your User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Completed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Completed",String.valueOf(sum));
                    }
                    else
                    {
                        String msg = "₹" + loanAmount + "was Failed to added to User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Payment Failed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Failed",String.valueOf(sum));
                    }
                    loanRepo.UpdateSantionedLoanApplication(app_id);
                    mailSender.LoanSanctionEmailSending(email,checkPG,loanAmount);
                }
                else
                {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Bank has rejected your PG loan application. Kindly check the information before submitting. ");
                    msg.append("You can also download the sample manual for verification.\n");
                    msg.append("Errors found:\n");

                    for (String mistake : checkPG) {
                        msg.append("- ").append(mistake).append("\n");
                    }

                    String finalMessage = msg.toString();
                    userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Failed", finalMessage);
                    System.out.println("Rejected");
                    loanRepo.UpdateRejectedLoanApplication(app_id);
                    mailSender.LoanSanctionEmailSending(email,checkPG,loanAmount);
                }
            }
            app_id = loanRepo.GettingapplicationId("ABROAD",a.replaceAll("ABROAD",""));
            System.out.println("APPPPPLICATIOJ ID : "+app_id);

            if(app_id.contains("ABROAD")) userId = loanRepo.gettingUserIDbyAppIDrepo(app_id);
            System.out.println("USERRR  ID : "+userId);
            if(a.contains("ABROAD"))
            {
                //check credentails
                List<String> checkPG = LoanCredentialofABROAD(a.replaceAll("ABROAD",""));
                System.out.println(checkPG);
                String email = userService.fetchingEmailService(userId);
                String loanAmount = userService.gettingLoanSancnedMoneyService(app_id);
                //update credentials(update table, Email,Notification,Money Sanction)
                if(checkPG.isEmpty())
                {
                    System.out.println("All are correct ------------");
                    String Generate_TransactionId = userService.generateTransactionId();
                    String last = Generate_TransactionId.length() >= 4 ? Generate_TransactionId.substring(Generate_TransactionId.length() - 4) : Generate_TransactionId;
                    String CurrentBalance = userService.CurrentBalanceService(userId);
                    long sum = Long.parseLong(CurrentBalance) + Long.parseLong(loanAmount);
                    boolean isUpdatedinDatabase1 = userService.UpdatingCurrentBalanceService(userId,String.valueOf(sum));
                    if(isUpdatedinDatabase1)
                    {

                        String msg = "SafeBharath Bank Transferred ₹" + loanAmount + " to your User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Completed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Completed",String.valueOf(sum));
                    }
                    else
                    {
                        String msg = "₹" + loanAmount + "was Failed to added to User ID: " + userId + " (Txn ID: xxxx" + last + ")";
                        userService.updatingFailedPaymentNotificationService(userId, "Payment Failed", msg);
                        userService.UpdateTransactionofSendMoneyService(Generate_TransactionId,"Bank",userId,loanAmount,"Failed",String.valueOf(sum));
                    }
                    loanRepo.UpdateSantionedLoanApplication(app_id);
                    mailSender.LoanSanctionEmailSending(email,checkPG,loanAmount);
                }
                else
                {
                    StringBuilder msg = new StringBuilder();
                    msg.append("Bank has rejected your ABROAD loan application. Kindly check the information before submitting. ");
                    msg.append("You can also download the sample manual for verification.\n");
                    msg.append("Errors found:\n");

                    for (String mistake : checkPG) {
                        msg.append("- ").append(mistake).append("\n");
                    }

                    String finalMessage = msg.toString();
                    userService.updatingFailedPaymentNotificationService(userId, "Bank Loan Sanction Failed", finalMessage);
                    loanRepo.UpdateRejectedLoanApplication(app_id);
                    System.out.println("Rejected");
                    mailSender.LoanSanctionEmailSending(email,checkPG,loanAmount);
                }
                //update credentials(update table, Email,Notification,Money Sanction)
            }

        }
    }
//    ========================================================================CREDENTIALS OF LOAN

    public List<String> LoanCredentialofUG(String s)
    {
        List<String> list = new ArrayList<>();
        LoanUg ug = loanRepo.getingAllDetailsofUG(s);
        String checkName = loanService.checkNameServiceUG(ug.getUserId(),ug.getName());
        String checkAge = loanService.checkAge(ug.getAge(),ug.getDob());
        String check10th = loanService.checkTenth(ug.getTenthPercentage());
        String check12th = loanService.checkTenth(ug.getTwelfthPercentage());
        String courseDuriation = loanService.checkCourseDuriationService(ug.getCourseDuration());
        String counselingCode  = loanService.CounselingCode(ug.getCounselingCode());
        String checkPan = loanService.checkPanSerive(ug.getUserId(),ug.getPanCard());
        String AnnualIncome = loanService.checkAnnualIncome(ug.getFatherIncome());
        String loanNeed = loanService.loanNeedService(ug.getApplicationId(),ug.getLoanAmount());
        if(!checkName.equals("Checked")) list.add("Name :"+ug.getName());
        if(!checkAge.equals("Checked")) list.add("Age : "+ug.getAge());
        if(!check10th.equals("Checked")) list.add("10th Percentage : "+ug.getTenthPercentage());
        if(!check12th.equals("Checked")) list.add("12th Percentage : "+ug.getTwelfthPercentage());
        if(!courseDuriation.equals("Checked")) list.add("Course Duration : "+ug.getCourseDuration());
        if(!counselingCode.equals("Checked")) list.add("Counseling Code : "+ug.getCounselingCode());
        if(!checkPan.equals("Checked")) list.add("Pan : "+ug.getPanCard());
        if(!AnnualIncome.equals("Checked")) list.add("Income : "+ug.getFatherIncome());
        if(!loanNeed.equals("Checked")) list.add("Loan Need : "+ug.getLoanAmount());
        return list;
    }

    public List<String> LoanCredentialofPG(String s)
    {
        List<String> list = new ArrayList<>();
        LoanPg pg = loanRepo.getingAllDetailsofPG(s);
        String checkName = loanService.checkNameServiceUG(pg.getUserId(),pg.getName());
        String checkAge = loanService.checkAge(pg.getAge(),pg.getDob());
        String check10th = loanService.checkTenth(pg.getTenthPercentage());
        String check12th = loanService.checkTenth(pg.getTwelfthPercentage());
        String cgpa = loanService.checkCGPA(pg.getFinalCgpa());
        String courseDuriation = loanService.checkCourseDuriationService(pg.getCourseDuration());
        String counselingCode  = loanService.CounselingCode(pg.getCounselingCode());
        String checkPan = loanService.checkPanSerive(pg.getUserId(),pg.getPanCard());
        String AnnualIncome = loanService.checkAnnualIncome(pg.getFatherIncome());
        String loanNeed = loanService.loanNeedService(pg.getApplicationId(),pg.getLoanAmount());
        System.out.println(
                "Check Name: " + checkName + "\n" +
                        "Check Age: " + checkAge + "\n" +
                        "10th Percentage Check: " + check10th + "\n" +
                        "12th Percentage Check: " + check12th + "\n" +
                        "CGPA Check: " + cgpa + "\n" +
                        "Course Duration Check: " + courseDuriation + "\n" +
                        "Counseling Code Check: " + counselingCode + "\n" +
                        "PAN Check: " + checkPan + "\n" +
                        "Annual Income Check: " + AnnualIncome + "\n" +
                        "Loan Amount Check: " + loanNeed
        );

        if(!checkName.equals("Checked")) list.add("Name :"+pg.getName());
        if(!checkAge.equals("Checked")) list.add("Age : "+pg.getAge());
        if(!check10th.equals("Checked")) list.add("10th Percentage : "+pg.getTenthPercentage());
        if(!check12th.equals("Checked")) list.add("12th Percentage : "+pg.getTwelfthPercentage());
        if(!courseDuriation.equals("Checked")) list.add("Course Duration : "+pg.getCourseDuration());
        if(!counselingCode.equals("Checked")) list.add("Counseling Code : "+pg.getCounselingCode());
        if(!checkPan.equals("Checked")) list.add("Pan : "+pg.getPanCard());
        if(!AnnualIncome.equals("Checked")) list.add("Income : "+pg.getFatherIncome());
        if(!loanNeed.equals("Checked")) list.add("Loan Need : "+pg.getLoanAmount());
        if(!cgpa.equals("Checked")) list.add("CGPA : "+pg.getFinalCgpa());
        return list;
    }

    public List<String> LoanCredentialofABROAD(String s)
    {
        LoanAbroad ab = loanRepo.getingAllDetailsofABROAD(s);
        List<String> list = new ArrayList<>();
        String checkName = loanService.checkNameServiceUG(ab.getUserId(),ab.getName());
        String checkAge = loanService.checkAge(ab.getAge(),ab.getDob());
        String check10th = loanService.checkTenth(ab.getTenthPercentage());
        String check12th = loanService.checkTenth(ab.getTwelfthPercentage());
        String applyfor = loanService.ApplyingForServie(ab.getApplyingFor());
        String courseDuriation = loanService.checkCourseDuriationService(ab.getCourseDuration());
        String checkPan = loanService.checkPanSerive(ab.getUserId(),ab.getPanCard());
        String AnnualIncome = loanService.checkAnnualIncome(ab.getFatherIncome());
        String loanNeed = loanService. loanNeedServiceAbroad(ab.getApplicationId(),ab.getLoanAmount(),ab.getApplyingFor());
        if(!checkName.equals("Checked")) list.add("Name :"+ab.getName());
        if(!checkAge.equals("Checked")) list.add("Age : "+ab.getAge());
        if(!check10th.equals("Checked")) list.add("10th Percentage : "+ab.getTenthPercentage());
        if(!check12th.equals("Checked")) list.add("12th Percentage : "+ab.getTwelfthPercentage());
        if(!courseDuriation.equals("Checked")) list.add("Course Duration : "+ab.getCourseDuration());
        if(!checkPan.equals("Checked")) list.add("Pan : "+ab.getPanCard());
        if(!AnnualIncome.equals("Checked")) list.add("Income : "+ab.getFatherIncome());
        if(!loanNeed.equals("Checked")) list.add("Loan Need : "+ab.getLoanAmount());
        if(!applyfor.equals("Checked")) list.add("Applying For : "+ab.getApplyingFor());
        return list;
    }

}
