package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.GetGroupPostsResponseDto;
import com.example.syncrowbackend.friend.dto.GetGroupsResponseDto;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.friend.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping("/groups/{groupId}/enter")
    public ResponseEntity<Long> groupEnter(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(groupService.groupEnter(groupId, userDetails.getUser()));
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GetGroupsResponseDto>> getGroupsByCategory(@RequestParam(required = false) GroupCategory category) {
        return ResponseEntity.ok(groupService.getGroupsByCategory(category));
    }

    @GetMapping("/groups/{groupId}/posts")
    public ResponseEntity<Page<GetGroupPostsResponseDto>> getGroupPostsByDesiredSize(
            @PathVariable Long groupId,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(groupService.getGroupPostsByDesiredSize(groupId, pageable));
    }

    @GetMapping("/user/groups")
    public ResponseEntity<List<GetGroupsResponseDto>> getParticipatingGroups(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(groupService.getParticipatingGroups(userDetails.getUser()));
    }
}
