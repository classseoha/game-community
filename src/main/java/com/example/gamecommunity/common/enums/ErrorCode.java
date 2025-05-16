package com.example.gamecommunity.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements BaseCode {

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST,"입력한 값의 형식이 잘못되었습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }

}
