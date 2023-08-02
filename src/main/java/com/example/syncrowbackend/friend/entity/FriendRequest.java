package com.example.syncrowbackend.friend.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
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

    @JoinColumn(name = "request_user_id")
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus status;

    public FriendRequest(User user, Post post) {
        this.requestUser = user;
        this.post = post;
        this.status = FriendRequestStatus.PROGRESS;
    }

    public void accepted() {
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void refused() {
        this.status = FriendRequestStatus.REFUSED;
    }
}
