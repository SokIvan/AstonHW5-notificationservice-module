package com.example.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Subject is mandatory")
    private String subject;

    @NotBlank(message = "Message is mandatory")
    private String message;
}