package com.example.syncrowbackend.friendrequestroom.controller;

import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friendrequestroom.service.FriendRequestPostService;
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
@RequestMapping("/post")
@RequiredArgsConstructor
public class FriendRequestPostController {

    @Autowired
    private final FriendRequestPostService postService;


    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@Valid @RequestBody FriendRequestPostDto postDto) {
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<FriendRequestPostDto> searchById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.searchById(id));
    }

    @GetMapping("/search/all")
    public ResponseEntity<Page<FriendRequestPostDto>> searchAll(@PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchAllPost(pageable));
    }

    @GetMapping("/list/user/{kakaoId}")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPostsByUser(@PathVariable String kakaoId, @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchPostsByUser(kakaoId, pageable));
    }

    @GetMapping("/search/keyword/")
    public ResponseEntity<Page<FriendRequestPostDto>> searchPosts(@RequestParam String keyword, @PageableDefault(size = 10, sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.searchByWord(keyword, pageable));
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updatePost(@Valid FriendRequestPostDto putRequestDto, @PathVariable Long id) {
        return ResponseEntity.ok(postService.updatePost(putRequestDto, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

}
