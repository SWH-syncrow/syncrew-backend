package com.example.syncrowbackend.friendrequestroom.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupFindResponseDto {
    private String name;
    private Long memberCount;
    private Long postCount;
    private List<PostDto> posts;


}
