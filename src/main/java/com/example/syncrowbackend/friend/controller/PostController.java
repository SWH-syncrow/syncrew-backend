package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.auth.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.*;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Long> createPost(@Valid @RequestBody PostRequestDto postDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.createPost(userDetails.getUser(), postDto));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(userDetails.getUser(), postId);
        return ResponseEntity.ok().build();
    }

    // 로그인한 사용자가 쓴 친구신청 post
    // (status) 로그인한 사용자가 신청 넣은 친구신청 post 정보
    @GetMapping("/user/posts")
    public ResponseEntity<Page<GetUserResponseDto>> getPostByCurrentUser(@RequestParam(required = false) FriendRequestStatus status, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return status==null ?
                ResponseEntity.ok(postService.getFriendRequestsByCurrentUser(userDetails.getUser(), pageable)) :
                ResponseEntity.ok(postService.getReceivedFriendRequestsByCurrentUser(userDetails.getUser(), status, pageable));
    }

    @GetMapping("/list/user/{kakaoId}")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPostsByUser(@PathVariable String kakaoId, @PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(kakaoId, pageable));
    }
}
