package com.example.notificationservice.service;

import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.model.NotificationLog;
import com.example.notificationservice.repository.NotificationLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.mail.username=test@test.com"
})
@Transactional
class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationLogRepository notificationLogRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void processUserEvent_UserCreated_ShouldSaveNotificationLog() {
        // Given
        UserEvent userEvent = new UserEvent();
        userEvent.setEventType("USER_CREATED");
        userEvent.setUserId(1L);
        userEvent.setUserName("Test User");
        userEvent.setUserEmail("test@example.com");
        userEvent.setEventTime(LocalDateTime.now());

        // When
        notificationService.processUserEvent(userEvent);

        // Then
        List<NotificationLog> logs = notificationLogRepository.findAll();
        assertEquals(1, logs.size());

        NotificationLog log = logs.get(0);
        assertEquals("USER_CREATED", log.getEventType());
        assertEquals("test@example.com", log.getUserEmail());
        assertTrue(log.getSubject().contains("Добро пожаловать"));
    }

    @Test
    void processUserEvent_UserDeleted_ShouldSaveNotificationLog() {
        // Given
        UserEvent userEvent = new UserEvent();
        userEvent.setEventType("USER_DELETED");
        userEvent.setUserId(1L);
        userEvent.setUserName("Test User");
        userEvent.setUserEmail("test@example.com");
        userEvent.setEventTime(LocalDateTime.now());

        // When
        notificationService.processUserEvent(userEvent);

        // Then
        List<NotificationLog> logs = notificationLogRepository.findAll();
        assertEquals(1, logs.size());

        NotificationLog log = logs.get(0);
        assertEquals("USER_DELETED", log.getEventType());
        assertEquals("test@example.com", log.getUserEmail());
        assertTrue(log.getSubject().contains("удален"));
    }
}