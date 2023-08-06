package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.auth.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendDto {
    private Long id;
    private String username;
    private String email;
    private String profileImage;
    private Double temp;

    public FriendDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.temp = user.getTemp();
    }
}
