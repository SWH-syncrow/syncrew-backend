package com.example.syncrowbackend.friend.dto;

import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetGroupsResponseDto {
    private Long id;
    private String name;
    private GroupCategory category;
    private Integer memberCount;
    private Integer postCount;

    @Builder
    public static GetGroupsResponseDto toDto(Group group) {
        return GetGroupsResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .category(group.getCategory())
                .memberCount(group.getUserGroups().size())
                .postCount(group.getPosts().size())
                .build();
    }
}
