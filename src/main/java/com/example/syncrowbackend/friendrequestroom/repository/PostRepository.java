package com.example.syncrowbackend.friendrequestroom.repository;

import com.example.syncrowbackend.friendrequestroom.entity.Post;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByUserId(Long id, Pageable pageable);
    Page<Post> findByGroupId_FriendRequestStatus(FriendRequestStatus status, Pageable pageable);

}