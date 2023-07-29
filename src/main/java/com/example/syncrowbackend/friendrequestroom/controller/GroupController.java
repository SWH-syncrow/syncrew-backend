package com.example.syncrowbackend.friendrequestroom.controller;

import com.example.syncrowbackend.friendrequestroom.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {

    // 그룹 전체 조회
    @GetMapping("/groups")
    public ResponseEntity<PostDto> searchAll() {
        return ResponseEntity.ok().build();
    }

    // 그룹 탐색
    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<PostDto> search(@PathVariable Long groupId, @RequestParam String page, @RequestParam String limit, @RequestParam String offset) {
        return ResponseEntity.ok().build();
    }

    // 참여중인 그룹 조회
    @GetMapping("/user/groups")
    public ResponseEntity<PostDto> searchJoinedGroups(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
