package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.user.entity.User;
import jakarta.persistence.*;

public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
