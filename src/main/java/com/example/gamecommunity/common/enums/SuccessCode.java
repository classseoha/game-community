package com.example.gamecommunity.common.enums;

import org.springframework.http.HttpStatus;

public enum SuccessCode implements BaseCode {

    // User
    CREATE_USER_SUCCESS(HttpStatus.CREATED,"유저를 생성했습니다."),

    // Post
    CREATE_POST_SUCCESS(HttpStatus.CREATED, "일정이 생성되었습니다."),
    GET_ALL_POST_SUCCESS(HttpStatus.OK, "일정 목록을 조회합니다."),
    GET_POST_SUCCESS(HttpStatus.FOUND,"일정 상세정보를 조회합니다."),
    UPDATE_POST_SUCCESS(HttpStatus.OK,"일정을 수정했습니다."),
    DELETE_POST_SUCCESS(HttpStatus.OK, "일정을 삭제했습니다.");



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
