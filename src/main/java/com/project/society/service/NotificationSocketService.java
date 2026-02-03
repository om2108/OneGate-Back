package com.project.society.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSocketService {

    private final SimpMessagingTemplate template;

    public void push(String userId) {

        template.convertAndSend(
                "/topic/notifications/" + userId,
                "refresh"
        );
    }
}

