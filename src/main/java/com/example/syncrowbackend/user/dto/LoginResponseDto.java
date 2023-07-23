package com.example.syncrowbackend.user.dto;

import com.example.syncrowbackend.global.jwt.TokenResponseDto;
import com.example.syncrowbackend.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private User user;
    private TokenResponseDto token;
}
