package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.common.entity.BaseTimeEntity;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestRoomStatus;
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
public class FriendRequestRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requestedId", referencedColumnName = "id")
    private User requestedId;

    @ManyToOne
    @JoinColumn(name = "acceptedId", referencedColumnName = "id")
    private User acceptedId;

    @Enumerated(EnumType.STRING)
    private FriendRequestRoomStatus friendRequestRoomStatus; // 신청 대기,수락,취소

}
