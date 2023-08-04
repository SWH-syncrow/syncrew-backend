package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.GetGroupPostsResponseDto;
import com.example.syncrowbackend.friend.dto.GetGroupsResponseDto;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.friend.service.GroupService;
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
public class GroupController {

    @Autowired
    private final GroupService groupService;

    @PostMapping("/groups/{groupId}/enter")
    public ResponseEntity<Long> groupEnter(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(groupService.groupEnter(groupId, userDetails.getUser()));
    }

    @GetMapping("/groups")
    public ResponseEntity<Page<GetGroupsResponseDto>> getGroupsByCategory(@RequestParam GroupCategory category, Pageable pageable) {
        return ResponseEntity.ok(groupService.getGroupsByCategory(category, pageable));
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GetGroupsResponseDto> getGroups(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.getGroups(groupId));
    }

    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<Page<GetGroupPostsResponseDto>> getGroupsByDesiredSize(@PathVariable Long groupId, @RequestParam Integer page, @RequestParam Integer limit, Pageable pageable) {
        return ResponseEntity.ok(groupService.getGroupsByDesiredSize(groupId, page, limit, pageable));
    }

    @GetMapping("/user/groups")
    public ResponseEntity<Page<GetGroupsResponseDto>> getParticipatingGroups(@AuthenticationPrincipal UserDetailsImpl userDetails, @PageableDefault(sort = "id",  direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(groupService.getParticipatingGroups(userDetails.getUser(), pageable));
    }
}
