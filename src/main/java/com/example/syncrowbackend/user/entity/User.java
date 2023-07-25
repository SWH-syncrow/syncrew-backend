package com.example.syncrowbackend.user.entity;

import com.example.syncrowbackend.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

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

    @Column(unique = true)
    @Size(min = 1, max = 30)
    private String nickname;

    private String phone_number;

    @Column(unique = true)
    private String email;

    private String role;

    private String status;

    private String digital_level;
}
