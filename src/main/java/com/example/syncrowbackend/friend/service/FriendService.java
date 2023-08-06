package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.FriendDto;
import com.example.syncrowbackend.friend.dto.FriendReactionDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.auth.entity.User;

public interface FriendService {
    void friendRequest(FriendRequestDto requestDto, User user);
    FriendDto acceptRequest(FriendReactionDto friendReactionDto, User user);
    void refuseRequest(FriendReactionDto friendReactionDto, User user);
}
