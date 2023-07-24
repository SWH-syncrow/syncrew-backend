package com.example.syncrowbackend.friendrequestroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class FriendRequestPostDTO {
    private Long id;

    @NotBlank(message = "친구신청 작성한 사람의 아이디가 필요합니다.")
    private String requestId;

    private String acceptId;

    @NotBlank(message = "게시물에 제목은 필요합니다. ")
    private String title;

    private String context;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

}
