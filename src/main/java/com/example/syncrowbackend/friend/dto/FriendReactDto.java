package com.example.syncrowbackend.friend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FriendReactDto {

    @NotBlank
    private Long friendRequestId;
}
