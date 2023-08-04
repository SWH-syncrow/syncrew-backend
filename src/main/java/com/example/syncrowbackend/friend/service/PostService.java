package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.*;
import com.example.syncrowbackend.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Long createPost(User user, PostRequestDto postDto);
    void deletePost(User user, Long postId);
    Page<GetUserResponseDto> getPostsWrittenByCurrentUser(User user, Pageable pageable);
    Page<GetUserResponseDto> getPostsRequestedByCurrentUser(User user, Pageable pageable);
    Page<FriendRequestPostDto> searchPostsByUser(String kakaoId, Pageable pageable);
}
