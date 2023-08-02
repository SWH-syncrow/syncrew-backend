package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.*;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.repository.FriendRequestRepository;
import com.example.syncrowbackend.friend.repository.GroupRepository;
import com.example.syncrowbackend.friend.repository.PostRepository;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger LOGGER = LoggerFactory.getLogger(PostServiceImpl.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public Long createPost(User user, PostRequestDto postDto) {
        LOGGER.info("createPost service 호출됨");
        Group group = findGroup(postDto.getGroupId());

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
    public void deletePost(User user, Long postId) {
        LOGGER.info("deletePost service 호출됨");
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
        LOGGER.info("getFriendRequestsByCurrentUser service 호출됨");
        /* 로그인한 사용자가 쓴 친구신청 post */
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(GetUserResponseDto::toDto);
    }

    @Override
    @Transactional
    public Page<GetUserResponseDto> getReceivedFriendRequestsByCurrentUser(User user, FriendRequestStatus status, Pageable pageable) {
        LOGGER.info("getReceivedFriendRequestsByCurrentUser service 호출됨");
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
        LOGGER.info("searchPostsByUser service 호출됨");
        User user = findUser(kakaoId);
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(FriendRequestPostDto::toDto);
    }


    @Transactional(readOnly = true)
    private User findUser(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "존재하지 않는 사용자입니다."));
    }

    @Transactional(readOnly = true)
    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "존재하지 않는 게시글입니다."));
    }

    @Transactional(readOnly = true)
    private Group findGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND_ERROR, "존재하지 않는 그룹입니다."));
    }
}
