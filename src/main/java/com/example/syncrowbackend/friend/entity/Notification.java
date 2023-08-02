package com.example.syncrowbackend.friend.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import com.example.syncrowbackend.friend.enums.NotificationStatus;
import com.example.syncrowbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(name = "\"read\"")
    private boolean read;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @ManyToOne
    private FriendRequest friendRequest;

    public Notification(User user, FriendRequest friendRequest, NotificationStatus status) {
        this.user = user;
        this.friendRequest = friendRequest;
        this.status = status;
        this.read = false;
    }

    public void updateReadStatus() {
        this.read = true;
    }
}
