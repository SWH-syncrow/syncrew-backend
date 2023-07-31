package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Page<FriendRequest> findByStatus(FriendRequestStatus status, Pageable pageable);
    // Page<FriendRequest> findByRequestUserIdAndStatus(Long userId, FriendRequestStatus status, Pageable pageable);

}