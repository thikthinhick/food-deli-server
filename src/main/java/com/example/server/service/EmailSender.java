package com.example.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;
    public void sendMail(String mail, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper resetPassword = new MimeMessageHelper(message, true);
        resetPassword.setSubject(subject);
        resetPassword.setFrom("chuongcoder@gmail.com");
        resetPassword.setTo(mail);
        String content = "<p>Mã otp: " + body + "</p>\n";
        resetPassword.setText(content, true);
        mailSender.send(message);
    }
}
