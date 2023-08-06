package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.auth.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.NotificationDto;
import com.example.syncrowbackend.friend.service.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUserNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<NotificationDto> notifications = notificationService.getNotifications(userDetails.getUser());
        return ResponseEntity.ok(notifications);
    }

    @PutMapping
    public ResponseEntity<Void> readNotifications(@RequestParam List<Long> ids, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.readNotifications(ids, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
