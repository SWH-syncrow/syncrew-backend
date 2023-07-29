package com.example.syncrowbackend.friendrequestroom.controller;

import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostRequestDto;
import com.example.syncrowbackend.friendrequestroom.dto.PostDto;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import com.example.syncrowbackend.friendrequestroom.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public ResponseEntity<Page<PostDto>> searchPostsByStatus(@RequestParam(required = false) FriendRequestStatus status, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(postService.searchByStatus(status, pageable));
        } else {
            return ResponseEntity.ok(postService.searchAllPost(pageable));
        }
    }

    @GetMapping("/list/user/{kakaoId}")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPostsByUser(@PathVariable String kakaoId, @PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(kakaoId, pageable));
    }
}
