package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.auth.entity.User;
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
    private Long friendId;
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

        User friend = getFriend(notification);
        this.friendId = friend.getId();
        this.friendName = friend.getUsername();
    }

    private User getFriend(Notification notification) {
        if (notification.getStatus() == NotificationStatus.REQUEST ||
                notification.getStatus() == NotificationStatus.ACCEPTED ||
                notification.getStatus() == NotificationStatus.REFUSED) {
            return notification.getFriendRequest().getPost().getUser();
        }
        return notification.getFriendRequest().getRequestUser();
    }
}
