package com.example.syncrowbackend.global.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
