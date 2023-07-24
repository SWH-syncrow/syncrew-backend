package com.example.syncrowbackend.user.dto;

import com.example.syncrowbackend.global.jwt.TokenResponseDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private UserResponseDto user;
    private TokenResponseDto token;
}
