package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.NotificationDto;
import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.repository.NotificationRepository;
import com.example.syncrowbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(NotificationDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void readNotification(Long id, User user) {
        Notification notification = findNotification(id);
        if(!notification.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.NOTIFICATION_WRONG_USER, "사용자의 알림만 읽을 수 있습니다.");
        }
        notification.updateReadStatus();
    }

    private Notification findNotification(Long id) {
        return notificationRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND_ERROR, "해당 알림이 존재하지 않습니다."));
    }
}
