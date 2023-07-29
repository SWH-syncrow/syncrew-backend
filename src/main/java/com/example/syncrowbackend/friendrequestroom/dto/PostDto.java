package com.example.syncrowbackend.friendrequestroom.dto;

import com.example.syncrowbackend.friendrequestroom.entity.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
                .title(post.getTitle())
                .content(post.getContent())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
}
