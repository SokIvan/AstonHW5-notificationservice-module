package com.example.notificationservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserEvent {
    private String eventType;
    private Long userId;
    private String userName;
    private String userEmail;
    private LocalDateTime eventTime;
}