package com.example.syncrowbackend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReissueRequestDto {
    @NotBlank
    private String refreshToken;
}
