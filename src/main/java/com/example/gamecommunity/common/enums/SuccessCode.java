package com.example.gamecommunity.common.enums;

import org.springframework.http.HttpStatus;

public enum SuccessCode implements BaseCode {

    // User
    CREATE_USER_SUCCESS(HttpStatus.CREATED,"유저를 생성했습니다."),

    // Post
    CREATE_POST_SUCCESS(HttpStatus.CREATED, "게시글이 생성되었습니다."),
    GET_ALL_POSTS_SUCCESS(HttpStatus.OK, "게시글 목록을 조회합니다."),
    UPDATE_POST_SUCCESS(HttpStatus.OK,"게시글을 수정했습니다."),
    DELETE_POST_SUCCESS(HttpStatus.OK, "게시글을 삭제했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String message) {
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

}
