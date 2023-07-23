package com.example.syncrowbackend.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "토큰이 유효하지 않습니다."),
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "토큰이 블랙리스트에 등록되어 로그인이 필요합니다."),
    EMPTY_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "A004", "Authorization 헤더에 토큰 값이 존재하지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A005", "Bearer 인증 타입이 아닙니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A006", "해당 토큰이 존재하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U001", "해당 사용자가 존재하지 않습니다.");

    private HttpStatus httpStatus;
    private String code;
    private String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
