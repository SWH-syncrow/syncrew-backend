package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.*;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    Long groupEnter(Long groupId, User user);
    List<GetGroupsResponseDto> getGroupsByCategory(GroupCategory category);
    GetGroupsResponseDto getGroups(Long groupId);
    Page<GetGroupPostsResponseDto> getGroupPostsByDesiredSize(Long groupId, Pageable pageable);
    List<GetGroupsResponseDto> getParticipatingGroups(User user);
}
