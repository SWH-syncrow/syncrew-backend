package com.example.syncrowbackend.friend.service;

import com.example.syncrowbackend.auth.entity.User;
import com.example.syncrowbackend.common.exception.CustomException;
import com.example.syncrowbackend.common.exception.ErrorCode;
import com.example.syncrowbackend.friend.dto.FriendDto;
import com.example.syncrowbackend.friend.dto.FriendReactionDto;
import com.example.syncrowbackend.friend.dto.FriendRequestDto;
import com.example.syncrowbackend.friend.entity.FriendRequest;
import com.example.syncrowbackend.friend.entity.Notification;
import com.example.syncrowbackend.friend.entity.Post;
import com.example.syncrowbackend.friend.enums.FriendRequestStatus;
import com.example.syncrowbackend.friend.enums.NotificationStatus;
import com.example.syncrowbackend.friend.repository.FriendRequestRepository;
import com.example.syncrowbackend.friend.repository.NotificationRepository;
import com.example.syncrowbackend.friend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final PostRepository postRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void friendRequest(FriendRequestDto requestDto, User user) {
        Post post = findPost(requestDto.getPostId());
        validateFriendRequest(user, post);

        FriendRequest friendRequest = friendRequestRepository.save(new FriendRequest(user, post));
        sendFriendRequestNotifications(friendRequest);
    }

    @Override
    @Transactional
    public FriendDto acceptRequest(FriendReactionDto reactionDto, User user) {
        FriendRequest friendRequest = findFriendRequest(reactionDto.getFriendRequestId());
        validateAcceptRequest(user, friendRequest);

        friendRequest.accepted();
        sendAcceptNotifications(friendRequest);

        return new FriendDto(friendRequest.getRequestUser());
    }

    @Override
    @Transactional
    public void refuseRequest(FriendReactionDto reactionDto, User user) {
        FriendRequest friendRequest = findFriendRequest(reactionDto.getFriendRequestId());
        validateRefuseRequest(user, friendRequest);

        friendRequest.refused();
        sendRefuseNotifications(friendRequest);
    }

    private void validateFriendRequest(User user, Post post) {
        List<FriendRequestStatus> excludedStatuses = Arrays.asList(FriendRequestStatus.ACCEPTED, FriendRequestStatus.PROGRESS);

        if (user.getId().equals(post.getUser().getId())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "자기 자신에게 친구 신청할 수 없습니다.");
        }

        if (friendRequestRepository.existsByPostAndStatusIn(post, excludedStatuses)) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "이미 다른 사용자가 친구 신청한 게시물입니다.");
        }

        if (friendRequestRepository.existsByRequestUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.DUPLICATED_FRIEND_REQUEST, "친구 신청은 중복 불가합니다.");
        }
    }

    private void validateAcceptRequest(User user, FriendRequest friendRequest) {
        if (!friendRequest.getPost().getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "자신이 받은 친구 신청에 대해서만 수락할 수 있습니다.");
        }

        if (friendRequest.getStatus() != FriendRequestStatus.PROGRESS) {
            throw new CustomException(ErrorCode.ALREADY_REACTED_FRIEND_REQUEST, "이미 수락 또는 거절한 친구 신청입니다.");
        }
    }

    private void validateRefuseRequest(User user, FriendRequest friendRequest) {
        if (!friendRequest.getPost().getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_NOT_GRANTED_ERROR, "자신이 받은 친구 신청에 대해서만 거절할 수 있습니다.");
        }

        if (friendRequest.getStatus() == FriendRequestStatus.REFUSED) {
            throw new CustomException(ErrorCode.ALREADY_REACTED_FRIEND_REQUEST, "이미 거절한 친구 신청입니다.");
        }
    }

    private void sendFriendRequestNotifications(FriendRequest friendRequest) {
        notificationRepository.save(new Notification(friendRequest.getRequestUser(), friendRequest, NotificationStatus.REQUEST));
        notificationRepository.save(new Notification(friendRequest.getPost().getUser(), friendRequest, NotificationStatus.REQUESTED));
    }

    private void sendAcceptNotifications(FriendRequest friendRequest) {
        notificationRepository.save(new Notification(friendRequest.getRequestUser(), friendRequest, NotificationStatus.ACCEPTED));
        notificationRepository.save(new Notification(friendRequest.getPost().getUser(), friendRequest, NotificationStatus.ACCEPT));
        deleteRequestedNotificationIfExists(friendRequest);
    }

    private void sendRefuseNotifications(FriendRequest friendRequest) {
        notificationRepository.save(new Notification(friendRequest.getRequestUser(), friendRequest, NotificationStatus.REFUSED));
        notificationRepository.save(new Notification(friendRequest.getPost().getUser(), friendRequest, NotificationStatus.REFUSE));
        deleteRequestedNotificationIfExists(friendRequest);
    }

    private void deleteRequestedNotificationIfExists(FriendRequest friendRequest) {
        notificationRepository.findByFriendRequestAndStatus(friendRequest, NotificationStatus.REQUESTED)
                        .ifPresent(notificationRepository::delete);
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
