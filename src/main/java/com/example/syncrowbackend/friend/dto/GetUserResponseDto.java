package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserResponseDto {
    private Long id;
    private String title;
    private String content;
    private WriterDto writer;

    @Builder
    public static GetUserResponseDto toDto(Post post) {
        return GetUserResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writer(WriterDto.toDto(post.getUser())).build();
    }
}
