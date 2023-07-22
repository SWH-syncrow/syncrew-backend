package com.example.syncrowbackend.friendrequestroom.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friendrequestroom.dto.FriendRequestPostDto;
import com.example.syncrowbackend.friendrequestroom.entity.FriendRequestPost;
import com.example.syncrowbackend.friendrequestroom.repository.FriendRequestPostRepository;
import com.example.syncrowbackend.user.entity.User;
import com.example.syncrowbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendRequestPostServiceImpl implements FriendRequestPostService {
    private final FriendRequestPostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FriendRequestPostDto searchById(Long id) {
        FriendRequestPost post = findPost(id);
        FriendRequestPostDto postDto = FriendRequestPostDto.toDto(post);
        return postDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FriendRequestPostDto> searchAllPost(Pageable pageable) {
        Page<FriendRequestPost> postsPage = postRepository.findAll(pageable);
        return postsPage.map(FriendRequestPostDto::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FriendRequestPostDto> searchPostsByUser(Long kakaoId, Pageable pageable) {
        User user = findUser(kakaoId);
        Page<FriendRequestPost> posts = postRepository.findByUserId(user.getId(), pageable);
        return posts.map(FriendRequestPostDto::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FriendRequestPostDto> searchByWord(String keyword, Pageable pageable) {
        Page<FriendRequestPost> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable);
        return posts.map(FriendRequestPostDto::toDto);
    }

    @Override
    public Long createPost(FriendRequestPostDto postDto) {
//        User user = SecurityUtil.getUser();
//        FriendRequestPost post = FriendRequestPost.toEntity(postDto, user);
//        postRepository.save(post);
//        return post.getId();
        return 1L;
    }

    @Override
    public Long updatePost(FriendRequestPostDto postDto, Long id) {
        FriendRequestPost post = findPost(id);
        Long userId = post.getUser().getId();
//        if (!userId.equals(SecurityUtil.getUser().getUserid())) {
//            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "해당 게시글을 수정할 수 있는 권한이 없습니다.");
//        }
        post.update(postDto.getTitle(), postDto.getContent());
        return userId;
    }

    @Override
    public void deletePost(Long id) {
        FriendRequestPost post = findPost(id);
//        Long userId = post.getUser().getId();
//        if (!userId.equals(SecurityUtil.getUser().getUserid())) {
//            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "해당 게시글을 삭제할 수 있는 권한이 없습니다.");
//        }
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    private User findUser(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR, "존재하지 않는 사용자입니다."));
    }

    @Transactional(readOnly = true)
    private FriendRequestPost findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "존재하지 않는 게시글입니다."));
    }
}
