package com.example.syncrowbackend.user.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.entity.UserGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();
}