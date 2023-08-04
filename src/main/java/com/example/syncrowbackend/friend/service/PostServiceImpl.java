package com.example.syncrowbackend.friend.service;

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
import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        // groupId가 현재 로그인한 user가 참여중인 그룹인지 검증
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
    public Page<GetUserResponseDto> getFriendRequestsByCurrentUser(User user, Pageable pageable) {
        /* 로그인한 사용자가 쓴 친구신청 post */
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(GetUserResponseDto::toDto);
    }

    @Override
    @Transactional
    public Page<GetUserResponseDto> getReceivedFriendRequestsByCurrentUser(User user, FriendRequestStatus status, Pageable pageable) {
        /* 로그인한 사용자가 신청넣은 친구신청 post 정보 */
        Page<FriendRequest> friendRequests = friendRequestRepository.findByRequestUserAndStatus(user, status, pageable);

        List<GetUserResponseDto> getUserResponseDtos = friendRequests.getContent().stream()
                .map(friendRequest -> {
                    Post post = friendRequest.getPost();
                    if (post == null) {
                        throw new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당 게시글을 찾을 수 없습니다.");
                    }
                    return GetUserResponseDto.toDto(post);
                }).collect(Collectors.toList());

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
