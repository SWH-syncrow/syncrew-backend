package com.example.syncrowbackend.friendrequestroom.dto;

import com.example.syncrowbackend.friendrequestroom.enums.FriendRequestRoomStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class FriendRequestRoomDto {
    private Long id;

    @NotBlank(message = "친구신청 작성한 사람의 아이디가 필요합니다.")
    private String requestId;

    private String acceptedId;

    @NotBlank(message = "친구 신청 상태는 필수 항목입니다.")
    private FriendRequestRoomStatus friendRequestRoomStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;
}
