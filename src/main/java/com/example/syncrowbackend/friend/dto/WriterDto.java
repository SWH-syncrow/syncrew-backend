package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WriterDto{
    private Long id;
    private String username;
    private String profileImage;
    private Double temp;

    @Builder
    public static WriterDto toDto(User user) {
        if(user == null)
            return null;

        return WriterDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileImage(user.getProfileImage())
                .temp(user.getTemp()).build();
    }
}
