package com.example.syncrowbackend.global.jwt;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
public class RefreshTokenDto {
    @NotBlank
    private String refreshToken;
}
