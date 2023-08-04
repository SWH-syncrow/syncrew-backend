package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsByRequestUserAndPost(User requestUser, Post post);
    Page<FriendRequest> findByRequestUserAndStatus(User requestUser, FriendRequestStatus status, Pageable pageable);
    List<FriendRequest> findByPostAndStatus(Post post, FriendRequestStatus friendRequestStatus);
    boolean existsByPostAndStatusIn(Post post, List<FriendRequestStatus> statuses);
}
