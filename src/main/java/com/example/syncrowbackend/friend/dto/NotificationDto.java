package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.enums.NotificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long friendRequestId;
    private String friendName;
    private boolean read;
    private NotificationStatus status;
    private LocalDateTime createdAt;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.friendRequestId = notification.getFriendRequest().getId();
        this.read = notification.isRead();
        this.status = notification.getStatus();
        this.createdAt = notification.getCreatedAt();

        if(notification.getStatus() == NotificationStatus.REQUEST ||
                notification.getStatus() == NotificationStatus.ACCEPTED ||
                notification.getStatus() == NotificationStatus.REFUSED) {
            this.friendName = notification.getFriendRequest().getPost().getUser().getUsername();
        } else {
            this.friendName = notification.getFriendRequest().getRequestUser().getUsername();
        }
    }
}