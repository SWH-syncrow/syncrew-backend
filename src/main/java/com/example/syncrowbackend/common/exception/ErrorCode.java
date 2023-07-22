package com.example.syncrowbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode{
    USER_NOT_FOUND_ERROR(400, "USER-NOT_FOUND", "USER NOT FOUND ERROR"),
    POST_NOT_FOUND_ERROR(400, "POST-NOT-FOUND", "POST NOT FOUND ERROR"),
    POST_NOT_DELETABLE(400, "POST_NOT_DELETABLE", "POST NOT DELETABLE"),
    FRIEND_REQUEST_ROOM_NOT_FOUND_ERROR(400, "FRIEND_REQUEST_ROOM_NOT_FOUND_ERROR", "FRIEND_REQUEST_ROOM NOT FOUND ERROR"),
    PERMISSION_NOT_GRANTED_ERROR(403, "PERMISSION-NOT-GRANTED", "PERMISSION NOT GRANTED ERROR"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR", "INTERNAL SERVER ERROR");

    private final int status;
    private final String errorType;
    private final String message;
}