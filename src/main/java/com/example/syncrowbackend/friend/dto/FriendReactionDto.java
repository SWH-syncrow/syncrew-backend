package com.example.syncrowbackend.friend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendReactionDto {

    @NotNull
    private Long friendRequestId;
}
