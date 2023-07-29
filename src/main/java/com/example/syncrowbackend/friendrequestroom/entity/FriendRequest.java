package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestStatus;
import com.example.syncrowbackend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "friend_request")
public class FriendRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_user_id", referencedColumnName = "id")
    private User requestUserId;

    @ManyToOne
    private Post post;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

}
