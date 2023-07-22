package com.example.syncrowbackend.user.entity;

import com.example.syncrowbackend.global.entity.BaseTimeEntity;
import com.example.syncrowbackend.user.dto.KakaoUserDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(unique = true, length = 50, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UserRole role;

    public User(KakaoUserDto kakaoUserDto) {
        this.username = kakaoUserDto.getName();
        this.email = kakaoUserDto.getEmail();
        this.role = UserRole.USER;
    }
}
