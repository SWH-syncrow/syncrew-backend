package com.example.syncrowbackend.user.dto;

import com.example.syncrowbackend.common.jwt.TokenResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private UserResponseDto user;
    private TokenResponseDto token;
    private Boolean isNewUser;
}
