package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    private Long id;
    @NotBlank(message = "게시물에 제목은 필요합니다.")
    private String title;
    private String content;
    private String username;
    private String profileImage;
    private Double temp;
    private List<Integer> rejectedUsers;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedAt;

    @Builder
    public static PostDto toDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .profileImage(post.getUser().getProfileImage())
                .temp(post.getUser().getTemp())
                .title(post.getTitle())
                .content(post.getContent())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    @Builder
    public static PostDto toDtoRej(Post post, List<Integer> rejectedUsers){
        return PostDto.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .profileImage(post.getUser().getProfileImage())
                .temp(post.getUser().getTemp())
                .title(post.getTitle())
                .content(post.getContent())
                .rejectedUsers(rejectedUsers)
                .modifiedAt(post.getModifiedAt())
                .build();
    }
}
