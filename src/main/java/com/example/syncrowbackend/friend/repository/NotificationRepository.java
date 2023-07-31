package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByCreatedAtDesc(User user);
}
