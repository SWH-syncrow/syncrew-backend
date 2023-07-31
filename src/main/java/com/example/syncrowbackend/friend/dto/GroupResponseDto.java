package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.enums.GroupCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupResponseDto {
    private Long id;
    @NotBlank(message = "그룹 이름은 필요합니다.")
    private String name;
    private GroupCategory category;
    private Long memberCount;
    private Long postCount;
}
