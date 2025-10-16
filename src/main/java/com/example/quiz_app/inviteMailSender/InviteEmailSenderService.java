package com.example.quiz_app.inviteMailSender;

import com.example.quiz_app.enums.Subject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class InviteEmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String from;

    public InviteEmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInviteEmail(String toEmail, String candidateName, Subject subject, String link) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        if (from != null && !from.isBlank()) {
            helper.setFrom(from);
        }
        helper.setSubject("HireQuiz - Your Quiz Invitation");
        helper.setText(buildEmailHtml(candidateName, subject, link), true);

        mailSender.send(message);
    }

    private String buildEmailHtml(String candidateName, Subject subject, String link)
    {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1'>" +
                "    <title>Quiz Invitation</title>" +
                "    <style>" +
                "        body { font-family: 'Trebuchet MS', Arial, sans-serif; background: #f7f9fc; margin: 0; padding: 0; text-align: center; }" +
                "        .container { max-width: 560px; margin: 40px auto; background: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0px 6px 20px rgba(0, 0, 0, 0.08); }" +
                "        .header { font-size: 22px; font-weight: bold; color: #2c3e50; letter-spacing: .5px; }" +
                "        .subject { margin: 8px 0 16px; font-size: 16px; color: #34495e; }" +
                "        .btn { display: inline-block; padding: 12px 24px; background: linear-gradient(90deg, #3498db, #2ecc71); color: #ffffff; text-decoration: none; border-radius: 8px; font-size: 16px; font-weight: bold; margin-top: 20px; transition: transform 0.2s ease; text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3); }" +
                "        .btn:hover { transform: scale(1.05); }" +
                "        .footer { margin-top: 20px; font-size: 13px; color: #7f8c8d; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>You're invited to take a quiz, " + escape(candidateName) + "!</div>" +
                "        <div class='subject'>Subject: <b>" + escape(subject.name()) + "</b></div>" +
                "        <a class='btn' href='" + link + "' target='_blank' rel='noopener'>Start Quiz</a>" +
                "        <div class='footer'>This link will expire in a few hours. Please do not share it.</div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
