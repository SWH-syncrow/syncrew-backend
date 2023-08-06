package com.example.syncrowbackend.auth.dto;

import com.example.syncrowbackend.auth.jwt.TokenDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private UserResponseDto user;
    private TokenDto token;
    private Boolean isNewUser;
}
