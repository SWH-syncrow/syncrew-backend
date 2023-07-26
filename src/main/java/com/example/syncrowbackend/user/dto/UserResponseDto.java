package com.example.syncrowbackend.user.dto;

import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String kakaoId;
    private String username;
    private String email;
    private String profileImage;
    private UserRole role;
    private Double temp;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .kakaoId(user.getKakaoId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profileImage(user.getProfileImage())
                .role(user.getRole())
                .temp(user.getTemp())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
