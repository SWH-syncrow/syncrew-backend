package com.example.syncrowbackend.friend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long postId;
}
