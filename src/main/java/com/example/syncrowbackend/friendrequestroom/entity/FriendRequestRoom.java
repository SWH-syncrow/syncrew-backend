package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.base.entity.BaseTimeEntity;
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
public class FriendRequestRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from", referencedColumnName = "id")
    private User from;

    @ManyToOne
    @JoinColumn(name = "to", referencedColumnName = "id")
    private User to;

    private FriendRequestRoomStatus friendRequestRoomStatus; // 신청 대기,수락,취소

}
