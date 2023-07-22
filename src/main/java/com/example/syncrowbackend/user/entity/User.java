package com.example.syncrowbackend.user.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @Column(nullable = false, unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String email;

    private String profileImage;

    @ColumnDefault(value = "36.5")
    private double temp;
}