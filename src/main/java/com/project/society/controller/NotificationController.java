package com.project.society.controller;

import com.project.society.model.Notification;
import com.project.society.model.ReadStatus;

import com.project.society.repository.NotificationRepository;

import com.project.society.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository repo;

    private final NotificationService service;

    private String uid(
            Authentication auth
    ){
        return auth.getName();
    }

    @GetMapping
    public List<Notification> all(
            Authentication auth
    ){

        return repo
                .findByTargetUserIdOrderByCreatedAtDesc(
                        uid(auth)
                );

    }

    @GetMapping("/count")
    public long count(
            Authentication auth
    ){

        return repo
                .countByTargetUserIdAndReadStatus(

                        uid(auth),

                        ReadStatus.UNREAD
                );

    }



    @PostMapping("/send")

    public Notification send(

            @RequestParam(required=false)
            String userId,

            @RequestParam
            String message,

            Authentication auth

    ){

        String target=

                userId!=null

                        ?

                        userId

                        :

                        auth.getName();

        return service.create(

                target,

                message

        );

    }

    @PutMapping("/{id}/read")
    public void read(
            @PathVariable String id
    ){

        service.markOne(
                id
        );

    }

    @PutMapping("/read-all")
    public void allRead(
            Authentication auth
    ){

        service.markAll(

                uid(auth)

        );

    }

}