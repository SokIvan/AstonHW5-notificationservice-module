package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationRequest;
import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.model.NotificationLog;
import com.example.notificationservice.repository.NotificationLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final NotificationLogRepository notificationLogRepository;

    @Transactional
    public void processUserEvent(UserEvent userEvent) {
        log.info("Processing user event: {} for user: {}",
                userEvent.getEventType(), userEvent.getUserEmail());

        String subject = "";
        String message = "";

        switch (userEvent.getEventType()) {
            case "USER_CREATED":
                subject = "Добро пожаловать на наш сайт!";
                message = String.format("Здравствуйте, %s! Ваш аккаунт на сайте был успешно создан.",
                        userEvent.getUserName());
                break;
            case "USER_DELETED":
                subject = "Ваш аккаунт был удален";
                message = String.format("Здравствуйте, %s! Ваш аккаунт на сайте был удален.",
                        userEvent.getUserName());
                break;
            default:
                log.warn("Unknown event type: {}", userEvent.getEventType());
                return;
        }

        sendNotification(userEvent.getUserEmail(), subject, message, userEvent.getEventType());
    }

    @Transactional
    public boolean sendDirectNotification(NotificationRequest request) {
        log.info("Sending direct notification to: {}", request.getEmail());
        return sendNotification(request.getEmail(), request.getSubject(),
                request.getMessage(), "DIRECT");
    }

    private boolean sendNotification(String email, String subject, String message, String eventType) {
        NotificationLog logEntry = new NotificationLog();
        logEntry.setEventType(eventType);
        logEntry.setUserEmail(email);
        logEntry.setSubject(subject);
        logEntry.setMessage(message);

        try {
            boolean success = emailService.sendEmail(email, subject, message);
            logEntry.setSuccess(success);

            if (!success) {
                logEntry.setErrorMessage("Email sending failed");
            }

            notificationLogRepository.save(logEntry);
            return success;

        } catch (Exception e) {
            logEntry.setSuccess(false);
            logEntry.setErrorMessage(e.getMessage());
            notificationLogRepository.save(logEntry);
            return false;
        }
    }
}