package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.friend.dto.*;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {

    Long groupEnter(Long groupId, User user);
    Page<GetGroupsResponseDto> getGroupsByCategory(GroupCategory category, Pageable pageable);
    Page<GetGroupPostsResponseDto> getGroupsByDesiredSize(Long groupId, Integer page, Integer limit, Pageable pageable);
    Page<GetGroupsResponseDto> getParticipatingGroups(User user, Pageable pageable);

}
