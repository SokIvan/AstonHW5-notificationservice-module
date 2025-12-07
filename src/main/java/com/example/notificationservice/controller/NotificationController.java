package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationRequest;
import com.example.notificationservice.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for sending notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send direct notification")
    public ResponseEntity<Map<String, Object>> sendNotification(
            @Valid @RequestBody NotificationRequest request) {

        boolean success = notificationService.sendDirectNotification(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ?
                "Notification sent successfully" : "Failed to send notification");

        return ResponseEntity.ok(response);
    }
}