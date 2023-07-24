package com.example.syncrowbackend.global.jwt;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
