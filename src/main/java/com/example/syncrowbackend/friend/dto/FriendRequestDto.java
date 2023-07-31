package com.example.syncrowbackend.friend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendRequestDto {

    @NotBlank
    private Long userId;

    @NotBlank
    private Long postId;
}
