package com.example.quiz_app.otpMailSender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpEmailSenderService {

    private final JavaMailSender mailSender;

    public OtpEmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final String SUBJECT = "HireQuiz - " +
            "Your One Time Password (OTP) for Login";

    public String generateOtp()
    {
        SecureRandom random = new SecureRandom();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);
    }

    public void sendOtpEmail(String toEmail, String otp) throws MessagingException
    {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(SUBJECT);
        helper.setText(buildEmailHtml(otp), true);

        mailSender.send(message);
    }

    private String buildEmailHtml(String otp)
    {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "    <title>OTP Verification</title>" +
                "    <style>" +
                "        body { font-family: 'Trebuchet MS', Arial, sans-serif; background: linear-gradient(135deg, #fdfbfb, #ebedee); margin: 0; padding: 0; text-align: center; }" +
                "        .container { max-width: 480px; margin: 50px auto; background: #ffffff; padding: 30px; border-radius: 15px; box-shadow: 0px 6px 15px rgba(0, 0, 0, 0.1); }" +
                "        .header { font-size: 24px; font-weight: bold; color: #2c3e50; letter-spacing: 1px; }" +
                "        .otp { font-size: 32px; font-weight: bold; color: #e67e22; background: #fff5e6; padding: 12px 24px; display: inline-block; border-radius: 8px; margin: 25px 0; box-shadow: inset 0px 0px 5px rgba(0,0,0,0.1); }" +
                "        .message { font-size: 16px; color: #34495e; line-height: 1.6; }" +
                "        .btn { display: inline-block; padding: 12px 25px; background: #3498db; color: #ffffff; text-decoration: none; border-radius: 8px; font-size: 16px; font-weight: bold; margin-top: 25px; transition: background 0.3s ease; }" +
                "        .btn:hover { background: #2980b9; }" +
                "        .footer { margin-top: 25px; font-size: 13px; color: #7f8c8d; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>🔐 OTP Verification</div>" +
                "        <p class='message'>Use the following One-Time Password (OTP) to Login:</p>" +
                "        <div class='otp'>" + otp + "</div>" +
                "        <p class='message'>This OTP is valid for only 10 minutes. Do not share it with anyone for security reasons.</p>" +
                "        <p class='footer'>If you did not request this, please ignore this email or contact our support team.</p>" +
                "    </div>" +
                "</body>" +
                "</html>";

    }
}
