package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.GetGroupPostsResponseDto;
import com.example.syncrowbackend.friend.dto.GetGroupsResponseDto;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.entity.UserGroup;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import com.example.syncrowbackend.friend.repository.FriendRequestRepository;
import com.example.syncrowbackend.friend.repository.GroupRepository;
import com.example.syncrowbackend.friend.repository.PostRepository;
import com.example.syncrowbackend.friend.repository.UserGroupRepository;
import com.example.syncrowbackend.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    @Transactional
    public Long groupEnter(Long groupId, User user) {
        Group group = findGroup(groupId);
        if (userGroupRepository.existsByUserAndGroup(user, group)) {
            throw new CustomException(ErrorCode.DUPLICATED_GROUP_ENTER, "사용자가 이미 참여중인 그룹입니다.");
        }
        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);
        return userGroup.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetGroupsResponseDto> getGroupsByCategory(GroupCategory category) {
        List<Group> groups;

        if (category == null) {
            groups = groupRepository.findAll();
        } else {
            groups = groupRepository.findByCategory(category);
        }

        return groups.stream()
                .map(GetGroupsResponseDto::toDto)
                .toList();
    }

    @Override
    public Page<GetGroupPostsResponseDto> getGroupPostsByDesiredSize(Long groupId, Pageable pageable) {
        Group group = findGroup(groupId);
        Page<Post> postsPage = postRepository.findByGroup(group, pageable);

        List<GetGroupPostsResponseDto> responseDtoList = postsPage.getContent()
                .stream()
                .map(post -> {
                    List<Long> rejectedUserIds = friendRequestRepository.findByPostAndStatus(post, FriendRequestStatus.REFUSED)
                            .stream()
                            .map(FriendRequest::getRequestUser)
                            .map(User::getId)
                            .toList();
                    return GetGroupPostsResponseDto.toDto(post, rejectedUserIds);
                })
                .toList();

        return new PageImpl<>(responseDtoList, pageable, postsPage.getTotalElements());
    }
    @Override
    public List<GetGroupsResponseDto> getParticipatingGroups(User user) {
        return userGroupRepository.findByUser(user)
                .stream()
                .map(UserGroup::getGroup)
                .map(GetGroupsResponseDto::toDto)
                .toList();
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() ->
                new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "존재하지 않는 그룹입니다."));
    }
}
