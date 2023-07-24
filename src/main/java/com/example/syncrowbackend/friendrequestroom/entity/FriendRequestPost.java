package com.example.syncrowbackend.friendrequestroom.entity;

import com.example.syncrowbackend.base.entity.BaseTimeEntity;
import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestPostCategory;
import com.example.syncrowbackend.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class FriendRequestPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    // @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    @NotNull
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String content;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private FriendRequestPostCategory friendRequestPostCategory;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
