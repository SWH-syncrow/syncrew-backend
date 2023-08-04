package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.enums.NotificationStatus;
import com.example.syncrowbackend.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
    List<Notification> findByIdIn(List<Long> ids);
    Optional<Notification> findByFriendRequestAndStatus(FriendRequest friendRequest, NotificationStatus notificationStatus);
}
