package com.example.syncrowbackend.friendrequestroom.repository;

import com.example.syncrowbackend.friendrequestroom.entity.FriendRequest;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Page<FriendRequest> findByStatus(FriendRequestStatus status, Pageable pageable);
}
