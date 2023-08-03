package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetGroupPostsResponseDto {
    private Long id;
    private String title;
    private String content;
    private WriterDto writerDto;
    private List<Long> rejectedUsers = new ArrayList<>();

    @Builder
    public static GetGroupPostsResponseDto toDto(Post post, List<Long> rejectedUsers) {
        return GetGroupPostsResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .writerDto(WriterDto.toDto(post.getUser()))
                .rejectedUsers(rejectedUsers)
                .build();
    }

}
