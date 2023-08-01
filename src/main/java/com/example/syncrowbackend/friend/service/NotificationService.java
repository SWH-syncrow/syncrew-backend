package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.NotificationDto;
import com.example.syncrowbackend.user.entity.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(User user);
    void readNotification(List<Long> ids, User user);
}
