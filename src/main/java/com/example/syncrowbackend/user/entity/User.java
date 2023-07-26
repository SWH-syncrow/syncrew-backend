package com.example.syncrowbackend.user.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String kakaoId;

    @NotBlank
    private String username;

    @Column(unique = true)
    private String email;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Double temp;
}