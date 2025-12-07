package com.example.notificationservice.service;

import com.example.notificationservice.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final NotificationService notificationService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void consumeUserEvent(UserEvent userEvent) {
        log.info("Received user event from Kafka: {}", userEvent.getEventType());

        try {
            notificationService.processUserEvent(userEvent);
            log.info("Successfully processed user event: {}", userEvent.getEventType());
        } catch (Exception e) {
            log.error("Error processing user event: {}", e.getMessage(), e);
        }
    }
}