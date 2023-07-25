package com.example.syncrowbackend.friendrequestroom.repository;

import com.example.syncrowbackend.friendrequestroom.entity.FriendRequestPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestPostRepository extends JpaRepository<FriendRequestPost, Long> {
    Page<FriendRequestPost> findAll(Pageable pageable);
    Page<FriendRequestPost> findByUserId(Long id, Pageable pageable);
    Page<FriendRequestPost> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
}
