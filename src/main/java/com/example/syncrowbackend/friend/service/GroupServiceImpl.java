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
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{

    private final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final PostRepository postRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public Long groupEnter(Long groupId, User user) {
        LOGGER.info("groupEnter service 호출됨");
        Group group = findGroup(groupId);

        UserGroup userGroup = UserGroup.builder()
                .user(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);
        return userGroup.getId();
    }

    @Override
    public Page<GetGroupsResponseDto> getGroupsByCategory(GroupCategory category, Pageable pageable) {
        LOGGER.info("getGroupsByCategory service 호출됨");
        Page<Group> groups = groupRepository.findByCategory(category, pageable);
        return groups.map(GetGroupsResponseDto::toDto);
    }

    @Override
    public Page<GetGroupPostsResponseDto> getGroupsByDesiredSize(Long groupId, Integer page, Integer limit, Pageable pageable) {
        LOGGER.info("getGroupsByDesiredSize service 호출됨");
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if(optionalGroup.isEmpty()){
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "해당 그룹을 찾을 수 없습니다.");
        }

        Page<Post> posts = postRepository.findByGroup(optionalGroup.get(), pageable);

        List<GetGroupPostsResponseDto> responseDtoList = new ArrayList<>();
        for (Post post : posts) {
            User user = post.getUser();
            if (user == null) {
                throw new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "해당 작성자를 찾을 수 없습니다.");
            }

            Page<FriendRequest> friendRequests = friendRequestRepository.findByPostId(post.getId());
            List<Long> rejectedUsers = new ArrayList<>();

            for (FriendRequest friendRequest : friendRequests) {
                if (friendRequest.getStatus() == FriendRequestStatus.REFUSED) {
                    rejectedUsers.add(friendRequest.getRequestUser().getId());
                }
            }
            responseDtoList.add(GetGroupPostsResponseDto.toDto(post, rejectedUsers));
        }
        return new PageImpl<>(responseDtoList, PageRequest.of(page, limit), posts.getTotalElements());
    }

    @Override
    public Page<GetGroupsResponseDto> getParticipatingGroups(User user, Pageable pageable) {
        LOGGER.info("getParticipatingGroups service 호출됨");
        Page<FriendRequest> friendRequests = friendRequestRepository.findByRequestUserAndStatus(user, FriendRequestStatus.ACCEPTED, pageable);

        List<GetGroupsResponseDto> getGroupsResponseDtos = friendRequests.getContent().stream()
                .map(friendRequest -> {
                    Post post = friendRequest.getPost();
                    if (post == null) {
                        throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당 게시글을 찾을 수 없습니다.");
                    }
                    Group group = post.getGroup();
                    if (group == null) {
                        throw new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "해당 그룹을 찾을 수 없습니다.");
                    }
                    return GetGroupsResponseDto.toDto(group);
                }).collect(Collectors.toList());

        return new PageImpl<>(getGroupsResponseDtos, pageable, friendRequests.getTotalElements());
    }


    @Transactional(readOnly = true)
    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "존재하지 않는 그룹입니다."));
    }
}
