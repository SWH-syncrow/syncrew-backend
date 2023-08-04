package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.FriendReactDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.friend.enums.FriendReaction;
import com.example.syncrowbackend.auth.entity.User;

public interface FriendService {
    void friendRequest(FriendRequestDto requestDto, User user);
    void friendReact(FriendReaction reaction, FriendReactDto friendReactDto, User user);
}
