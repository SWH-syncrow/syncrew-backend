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
    private String username;
    private String email;
    private String profileImage;
    private Double temp;
    private Boolean isTestTarget;

    public UserResponseDto(User user, boolean isTestTarget) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.temp = user.getTemp();
        this.isTestTarget = isTestTarget;
    }
}
