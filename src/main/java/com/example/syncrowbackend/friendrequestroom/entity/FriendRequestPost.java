package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.base.entity.BaseTimeEntity;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestPostCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FriendRequestPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private String user_id;

    @NotBlank
    private String title;

    @NotNull
    private String content;

    @NotBlank
    private FriendRequestPostCategory category;

}
