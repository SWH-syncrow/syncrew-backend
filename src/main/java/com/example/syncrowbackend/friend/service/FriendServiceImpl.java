package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.FriendReactDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendReaction;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.enums.NotificationStatus;
import com.example.syncrowbackend.friend.repository.FriendRequestRepository;
import com.example.syncrowbackend.friend.repository.NotificationRepository;
import com.example.syncrowbackend.friend.repository.PostRepository;
import com.example.syncrowbackend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final PostRepository postRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void friendRequest(FriendRequestDto requestDto, User user) {
        if (!requestDto.getUserId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_WRONG_USER, "로그인한 사용자와 일치하지 않는 user id 입니다.");
        }

        Post post = findPost(requestDto.getPostId());

        if (friendRequestRepository.existsByRequestUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.DUPLICATED_FRIEND_REQUEST, "친구 요청은 중복 불가합니다.");
        }

        FriendRequest friendRequest = friendRequestRepository.save(new FriendRequest(user, post));

        notificationRepository.save(new Notification(user, friendRequest, NotificationStatus.REQUEST));
        notificationRepository.save(new Notification(post.getUser(), friendRequest, NotificationStatus.REQUESTED));
    }

    @Override
    @Transactional
    public void friendReact(FriendReaction reaction, FriendReactDto friendReactDto, User user) {
        FriendRequest friendRequest = findFriendRequest(friendReactDto.getFriendRequestId());

        if (!friendRequest.getPost().getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FRIEND_REACT_WRONG_USER, "자신이 받은 친구 요청에 대해서만 수락 또는 거절할 수 있습니다.");
        }

        if (friendRequest.getStatus() != FriendRequestStatus.PROGRESS) {
            throw new CustomException(ErrorCode.FRIEND_REQUEST_ALREADY_REACTED, "이미 수락 또는 거절한 친구 신청입니다.");
        }

        if (reaction == FriendReaction.ACCEPT) {
            friendRequest.accepted();
            notificationRepository.save(new Notification(user, friendRequest, NotificationStatus.ACCEPT));
            notificationRepository.save(new Notification(friendRequest.getRequestUser(), friendRequest, NotificationStatus.ACCEPTED));
        } else {
            friendRequest.refused();
            notificationRepository.save(new Notification(user, friendRequest, NotificationStatus.REFUSE));
            notificationRepository.save(new Notification(friendRequest.getRequestUser(), friendRequest, NotificationStatus.REFUSED));
        }

        Notification requested = notificationRepository.findByFriendRequestAndStatus(friendRequest, NotificationStatus.REQUESTED)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND_ERROR, "요청받은 상태의 알림이 존재하지 않아 삭제 불가합니다."));

        notificationRepository.delete(requested);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.POST_NOT_FOUND_ERROR, "해당 신청글이 존재하지 않습니다."));
    }

    private FriendRequest findFriendRequest(Long id) {
        return friendRequestRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.FRIEND_REQUEST_NOT_FOUND_ERROR, "해당 친구 신청이 존재하지 않습니다."));
    }
}
