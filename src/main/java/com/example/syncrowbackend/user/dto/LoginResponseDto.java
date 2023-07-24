package com.example.syncrowbackend.user.dto;

import com.example.syncrowbackend.global.jwt.TokenResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private UserResponseDto user;
    private TokenResponseDto token;
}
