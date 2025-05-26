package com.example.bankSystem.service;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.*;

import java.util.List;
import java.util.Properties;
@Service
@Async
public class MailSender
{
    public void sending(String email,String otp)
    {

        System.out.println("OTP REQUIREMENTS :"+otp);

        // Sender's email credentials
        final String senderEmail = "safebharathbank@gmail.com";
        final String password = "pcyi kksz lpca cgvu";

        // SMTP server configuration
        String host = "smtp.gmail.com"; // Change for other email services
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Create a session with the authentication
        Session session = Session.getInstance(properties,new Authenticator()
        {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(senderEmail, password);
            }
        });
        try
        {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Don't share this otp with anyone");
            message.setText("Your OTP : "+otp);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!✅");
        }
        catch(MessagingException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void AutoApprovalEmailSending(String fullName, String email, String aadhar) {
        // Sender's email credentials
        final String senderEmail = "safebharathbank@gmail.com";
        final String password = "pcyi kksz lpca cgvu";

        // SMTP server configuration
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Create a session with the authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("🎉 Welcome to SafeBharat Bank - Your Account is Activated!");

            // Craft welcome message
            String welcomeMessage = "Dear " + fullName + ",\n\n"
                    + "We are delighted to welcome you to SafeBharat Bank! 🎉\n\n"
                    + "Your account associated with Aadhar number " + aadhar + " has been successfully activated.\n\n"
                    + "You can now log in to your account and start using our services immediately.\n\n"
                    + "If you have any questions or need support, feel free to reach out to us.\n\n"
                    + "Warm regards,\n"
                    + "SafeBharat Bank Team";

            message.setText(welcomeMessage);

            // Send the email
            Transport.send(message);
            System.out.println("Welcome email sent successfully to " + email + " ✅");

        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    public void LoanSanctionEmailSending(String email, List<String> mistakes, String sanctionedAmount) {
        final String senderEmail = "safebharathbank@gmail.com";
        final String password = "pcyi kksz lpca cgvu";

        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            String subject;
            StringBuilder content = new StringBuilder();

            if (mistakes != null && !mistakes.isEmpty()) {
                // Rejection Email
                subject = "Loan Application Rejected - SafeBharat Bank";
                content.append("Dear Applicant,\n\n");
                content.append("We regret to inform you that your loan application has been rejected.\n");
                content.append("Please review the following issues found in your submission:\n\n");

                for (String mistake : mistakes) {
                    content.append("- ").append(mistake).append("\n");
                }

                content.append("\nYou may download the sample manual to help fix these issues.");
                content.append("\nAfter correcting them, you can resubmit your application for review.");
                content.append("\n\nRegards,\nSafeBharat Bank");

            } else {
                // Sanction Email
                subject = "Loan Sanctioned Successfully - SafeBharat Bank";
                content.append("Dear Applicant,\n\n");
                content.append("Congratulations! Your loan application has been approved.\n");
                content.append("Sanctioned Amount: ₹").append(sanctionedAmount).append("\n\n");
                content.append("The sanctioned amount will be credited shortly to your bank account.\n");
                content.append("Please check your profile for more updates.\n\n");
                content.append("Regards,\nSafeBharat Bank");
            }

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(content.toString());

            Transport.send(message);
            System.out.println("Email sent successfully ✅");

        } catch (MessagingException e) {
            System.out.println("Failed to send email ❌: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void ByeByeMessage(String email)
    {
        // Sender's email credentials
        final String senderEmail = "safebharathbank@gmail.com";
        final String password = "pcyi kksz lpca cgvu";

        // SMTP server configuration
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Create a session with the authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("SafeBharath Account Deletion Confirmation");

            String body = "Hello,\n\n"
                    + "This is to confirm that your SafeBharath Bank account has been permanently deleted as per your request.\n\n"
                    + "We're sad to see you go, but we respect your decision.\n"
                    + "If you ever wish to bank with us again, we'll be happy to welcome you back.\n\n"
                    + "Take care and see you again!\n"
                    + "— SafeBharath Bank Team";

            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Account deletion email sent successfully! ✅");
        }
        catch (MessagingException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }


}
