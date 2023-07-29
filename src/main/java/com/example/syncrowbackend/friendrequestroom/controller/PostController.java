package com.example.syncrowbackend.friendrequestroom.controller;

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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping("/posts/{kakaoId}")
    public ResponseEntity<Long> createPost(@PathVariable String kakaoId, @Valid @RequestBody PostRequestDto postDto) {
        return ResponseEntity.ok(postService.createPost(kakaoId, postDto));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(String kakaoId, @PathVariable Long postId) {
        postService.deletePost(kakaoId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/posts")
    public ResponseEntity<Page<PostDto>> searchAllPost(@PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchAllPost(pageable));
    }

    @GetMapping("/user/posts")
    public ResponseEntity<Page<PostDto>> searchByStatus(@RequestParam FriendRequestStatus status, @PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchByStatus(status, pageable));
    }

    @GetMapping("/list/user/{kakaoId}")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPostsByUser(@PathVariable String kakaoId, @PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(kakaoId, pageable));
    }
}
