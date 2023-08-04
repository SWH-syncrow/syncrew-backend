package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Page<FriendRequest> findByPostId(Long postId, Pageable pageable);
    boolean existsByRequestUserAndPost(User requestUser, Post post);
    Page<FriendRequest> findByRequestUserAndStatus(User requestUser, FriendRequestStatus status, Pageable pageable);

    List<FriendRequest> findByPostAndStatus(Post post, FriendRequestStatus friendRequestStatus);
}
