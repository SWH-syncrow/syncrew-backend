package com.example.syncrowbackend.friendrequestroom.dto;

import com.example.syncrowbackend.friendrequestroom.enums.GroupCategory;
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
    private String name;
    private GroupCategory category;
    private Long memberCount;
    private Long postCount;
}
