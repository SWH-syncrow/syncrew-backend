package com.example.syncrowbackend.auth.dto;

import com.example.syncrowbackend.auth.jwt.TokenResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private UserResponseDto user;
    private TokenResponseDto token;
    private Boolean isNewUser;
}
