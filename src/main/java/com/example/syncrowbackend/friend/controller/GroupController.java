package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.friend.dto.GroupResponseDto;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.friend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {

    @Autowired
    private final GroupService groupService;

//    // 그룹 입장
//    @PostMapping("/groups/{groupId}/enter")
//    public ResponseEntity<Void> enterGroup(@PathVariable Long groupId){
//        return ResponseEntity.ok(groupService.enterGroup(groupId));
//    }
//
//    // 그룹 전체 조회
//    @GetMapping("/groups")
//    public ResponseEntity<GroupResponseDto> searchAllGroup(@RequestParam GroupCategory category) {
//        return ResponseEntity.ok(groupService.searchAllGroup()).build();
//    }
//
//    // 그룹 탐색
//    @GetMapping("/groups/{groupId}/posts")
//    public ResponseEntity<GroupResponseDto> searchGroupById(@PathVariable Long groupId, @RequestParam Long page, @RequestParam Long limit, @RequestParam Long offset) {
//        return ResponseEntity.ok().build();
//    }

//    // 참여중인 그룹 조회
//    @GetMapping("/user/groups")
//    public ResponseEntity<GroupResponseDto> searchJoinedGroups(@PathVariable Long id) {
//        return ResponseEntity.ok().build();
//    }
}
