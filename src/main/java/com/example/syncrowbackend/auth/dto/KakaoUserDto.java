package com.example.syncrowbackend.auth.dto;

import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.entity.UserRole;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserDto {
    private String id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
        private String profileImage;
    }

    @ToString
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoAccount {
        private String email;
    }

    public User toEntity() {
        return User.builder()
                .kakaoId(id)
                .username(properties.nickname)
                .email(kakaoAccount.email)
                .profileImage(properties.profileImage)
                .role(UserRole.USER)
                .temp(36.5)
                .build();
    }
}
