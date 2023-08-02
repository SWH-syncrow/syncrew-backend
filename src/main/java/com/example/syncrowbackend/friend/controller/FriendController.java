package com.example.syncrowbackend.friend.controller;

import com.example.syncrowbackend.common.security.UserDetailsImpl;
import com.example.syncrowbackend.friend.dto.FriendReactDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.friend.enums.FriendReaction;
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

    @PostMapping
    public ResponseEntity<Void> friendReact(@RequestParam("q") FriendReaction reaction,
                                            @RequestBody @Valid FriendReactDto reactDto,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        friendService.friendReact(reaction, reactDto, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
