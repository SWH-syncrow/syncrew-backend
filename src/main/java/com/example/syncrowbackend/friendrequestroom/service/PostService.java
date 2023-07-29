package com.example.syncrowbackend.friendrequestroom.service;

import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostRequestDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostDto;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PostService {
    Long createPost(String kakaoId, PostRequestDto postDto);
    void deletePost(String kakaoId, Long postId);
    Page<PostDto> searchAllPost(Pageable pageable);
    Page<PostDto> searchByStatus(FriendRequestStatus status, Pageable pageable);
    Page<FriendRequestPostDto> searchPostsByUser(String kakaoId, Pageable pageable);

}
