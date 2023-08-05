package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.repository.UserRepository;
import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friend.dto.GetUserResponseDto;
import com.example.syncrowbackend.friend.dto.PostRequestDto;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.repository.FriendRequestRepository;
import com.example.syncrowbackend.friend.repository.GroupRepository;
import com.example.syncrowbackend.friend.repository.PostRepository;
import com.example.syncrowbackend.friend.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    @Transactional
    public Long createPost(User user, PostRequestDto postDto) {
        Group group = findGroup(postDto.getGroupId());
        if (!userGroupRepository.existsByUserAndGroup(user, group)) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "사용자가 참여중인 그룹이 아닙니다.");
        }

        Post post = Post.builder()
                .user(user)
                .group(group)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .build();

        postRepository.save(post);
        return post.getId();
    }

    @Override
    @Transactional
    public void deletePost(User user, Long postId) {
        Post post = findPost(postId);
        Long userId = post.getUser().getId();
        if (!userId.equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "해당 게시글을 삭제할 수 있는 권한이 없습니다.");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetUserResponseDto> getPostsWrittenByCurrentUser(User user, Pageable pageable) {
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(GetUserResponseDto::toDto);
    }

    @Override
    @Transactional
    public Page<GetUserResponseDto> getPostsRequestedByCurrentUser(User user, Pageable pageable) {
        List<FriendRequestStatus> includedStatuses = Arrays.asList(FriendRequestStatus.ACCEPTED, FriendRequestStatus.PROGRESS);
        Page<FriendRequest> friendRequests = friendRequestRepository.findByRequestUserAndStatusIn(user, includedStatuses, pageable);

        List<GetUserResponseDto> getUserResponseDtos = friendRequests.getContent()
                .stream()
                .map(FriendRequest::getPost)
                .map(GetUserResponseDto::toDto)
                .toList();

        return new PageImpl<>(getUserResponseDtos, pageable, friendRequests.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FriendRequestPostDto> searchPostsByUser(String kakaoId, Pageable pageable) {
        User user = findUser(kakaoId);
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(FriendRequestPostDto::toDto);
    }

    private User findUser(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "존재하지 않는 사용자입니다."));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "존재하지 않는 게시글입니다."));
    }

    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "존재하지 않는 그룹입니다."));
    }
}
