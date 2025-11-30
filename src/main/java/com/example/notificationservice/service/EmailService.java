package com.example.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(String to, String subject, String message) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(fromEmail);
            email.setTo(to);
            email.setSubject(subject);
            email.setText(message);

            mailSender.send(email);
            log.info("Email sent successfully to: {}", to);
            return true;
        } catch (Exception e) {
            log.error("Failed to send email to: {}, error: {}", to, e.getMessage());
            return false;
        }
    }
}