package com.scms.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendToUser(String email, String type, String message) {
        Map<String, String> payload = buildPayload(type, message);
        messagingTemplate.convertAndSendToUser(email, "/queue/notifications", payload);
    }

    public void broadcast(String type, String message) {
        Map<String, String> payload = buildPayload(type, message);
        messagingTemplate.convertAndSend("/topic/announcements", payload);
    }

    private Map<String, String> buildPayload(String type, String message) {
        return Map.of(
                "type", type,
                "message", message,
                "time", LocalDateTime.now().toString()
        );
    }
}