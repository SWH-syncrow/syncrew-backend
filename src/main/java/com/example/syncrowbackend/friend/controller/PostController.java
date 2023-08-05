package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.auth.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.*;
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

    @GetMapping("/user/posts")
    public ResponseEntity<Page<GetUserResponseDto>> getPostsWrittenByCurrentUser(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(postService.getPostsWrittenByCurrentUser(userDetails.getUser(), pageable));
    }

    @GetMapping("/user/requests")
    public ResponseEntity<Page<GetUserResponseDto>> getPostsRequestedByCurrentUser(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.ok(postService.getPostsRequestedByCurrentUser(userDetails.getUser(), pageable));
    }

    @GetMapping("/list/user/{kakaoId}")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPostsByUser(@PathVariable String kakaoId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(kakaoId, pageable));
    }
}
