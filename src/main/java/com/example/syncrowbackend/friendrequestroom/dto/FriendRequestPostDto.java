package com.example.syncrowbackend.friendrequestroom.dto;

import com.example.syncrowbackend.friendrequestroom.entity.FriendRequestPost;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestPostDto {
    private Long id;

    @NotNull(message = "친구신청 작성한 사람의 아이디가 필요합니다.")
    private Long userId;

    @NotBlank(message = "게시물에 제목은 필요합니다.")
    private String title;

    private String content;
    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedAt;

    @Builder
    public static FriendRequestPostDto toDto(FriendRequestPost post) {
        return FriendRequestPostDto.builder()
                .id(post.getId())
                .userId(post.getUser().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

}
