package com.example.syncrowbackend.friend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendReactDto {

    @NotBlank
    private Long friendRequestId;
}
