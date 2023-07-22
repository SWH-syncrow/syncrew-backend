package com.example.syncrowbackend.friendrequest.entity;

import com.example.syncrowbackend.base.entity.BaseTimeEntity;
import com.example.syncrowbackend.user.entity.User;
import jakarta.persistence.*;

@Entity
public class Friendrequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_id", referencedColumnName = "id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_id", referencedColumnName = "id")
    private User toUser;

    private String status;
}
