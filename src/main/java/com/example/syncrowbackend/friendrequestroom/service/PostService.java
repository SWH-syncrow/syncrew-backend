package com.example.syncrowbackend.friendrequestroom.service;

import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostRequestDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostDto;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import com.example.syncrowbackend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PostService {
    Long createPost(User user, PostRequestDto postDto);
    void deletePost(User user, Long postId);
    Page<PostDto> searchAllPost(Pageable pageable);
    Page<PostDto> searchByStatus(FriendRequestStatus status, Pageable pageable);
    Page<FriendRequestPostDto> searchPostsByUser(String kakaoId, Pageable pageable);

}
