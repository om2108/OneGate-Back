package com.project.society.service;

import com.project.society.model.Notification;

import com.project.society.model.ReadStatus;

import com.project.society.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repo;

    private final NotificationSocketService socket;

    public Notification create(

            String userId,

            String message

    ) {

        Notification n =

                new Notification(

                        null,

                        message,

                        userId,

                        ReadStatus.UNREAD,

                        LocalDateTime.now(),

                        LocalDateTime.now()

                );

        Notification saved =

                repo.save(
                        n
                );

        socket.push(
                userId
        );

        return saved;

    }

    public void markOne(
            String id
    ) {

        Notification n =

                repo.findById(id)

                        .orElseThrow();

        n.setReadStatus(
                ReadStatus.READ
        );

        repo.save(
                n
        );

    }

    public void markAll(
            String userId
    ) {

        List<Notification> list =

                repo

                        .findByTargetUserIdAndReadStatusOrderByCreatedAtDesc(

                                userId,

                                ReadStatus.UNREAD

                        );

        list.forEach(

                n ->

                        n.setReadStatus(

                                ReadStatus.READ

                        )

        );

        repo.saveAll(
                list
        );

    }

}