package com.example.syncrowbackend.global.jwt;

import lombok.*;

@Data
@NoArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
