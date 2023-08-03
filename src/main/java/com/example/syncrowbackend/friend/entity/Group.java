package com.example.syncrowbackend.friend.entity;

import com.example.syncrowbackend.friend.enums.GroupCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private GroupCategory category;

    @OneToMany(mappedBy = "group")
    private List<Post> posts;

    @OneToMany(mappedBy = "group")
    private List<UserGroup> userGroups;
}

