package com.example.notificationservice.controller;

import com.example.notificationservice.dto.NotificationRequest;
import com.example.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for sending notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "Send direct notification")
    public ResponseEntity<EntityModel<Map<String, Object>>> sendNotification(
            @Valid @RequestBody NotificationRequest request) {

        boolean success = notificationService.sendDirectNotification(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", success ?
                "Notification sent successfully" : "Failed to send notification");
        response.put("email", request.getEmail());

        EntityModel<Map<String, Object>> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(NotificationController.class).sendNotification(request)).withSelfRel());
        resource.add(linkTo(methodOn(NotificationController.class.getClass()).getClass()).withRel("send-another"));

        return ResponseEntity.ok(resource);
    }
}