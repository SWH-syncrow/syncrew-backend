package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.NotificationDto;
import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.repository.NotificationRepository;
import com.example.syncrowbackend.auth.entity.User;
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
    public void readNotification(List<Long> ids, User user) {
        List<Notification> notifications = notificationRepository.findByIdIn(ids);

        for (Notification notification : notifications) {
            if (!notification.getUser().getId().equals(user.getId())) {
                throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "사용자의 알림만 읽음 처리할 수 있습니다.");
            }
            notification.updateReadStatus();
        }
    }
}
