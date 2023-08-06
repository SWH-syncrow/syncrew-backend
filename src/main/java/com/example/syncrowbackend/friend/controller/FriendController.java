package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.auth.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.FriendReactionDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.friend.dto.FriendDto;
import com.example.syncrowbackend.friend.service.FriendServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendServiceImpl friendService;

    @PostMapping("/request")
    public ResponseEntity<Void> friendRequest(@RequestBody @Valid FriendRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        friendService.friendRequest(requestDto, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept")
    public ResponseEntity<FriendDto> acceptRequest(@RequestBody @Valid FriendReactionDto reactDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FriendDto friend = friendService.acceptRequest(reactDto, userDetails.getUser());
        return ResponseEntity.ok(friend);
    }

    @PostMapping("/refuse")
    public ResponseEntity<Void> refuseRequest(@RequestBody @Valid FriendReactionDto reactDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        friendService.refuseRequest(reactDto, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
