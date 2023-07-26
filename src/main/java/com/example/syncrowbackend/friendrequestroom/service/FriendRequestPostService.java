package com.example.syncrowbackend.friendrequestroom.service;

import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface FriendRequestPostService {
    FriendRequestPostDto searchById(Long id);
    Page<FriendRequestPostDto> searchAllPost(Pageable pageable);
    Page<FriendRequestPostDto> searchPostsByUser(String kakaoId, Pageable pageable);
    Page<FriendRequestPostDto> searchByWord(String keyword, Pageable pageable);


    Long createPost(FriendRequestPostDto postDto);
    Long updatePost(FriendRequestPostDto postDto, Long id);
    void deletePost(Long id);

}
