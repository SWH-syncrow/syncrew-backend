package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.friendrequestroom.enums.GroupCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private GroupCategory category;
}
