package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.NotificationDto;
import com.example.syncrowbackend.auth.entity.User;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(User user);
    void readNotifications(List<Long> ids, User user);
}
