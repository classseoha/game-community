package com.example.gamecommunity.common.exception;

import com.example.gamecommunity.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {

        // 메시지만 전달
        super(errorCode.getMessage());

        // 상태코드는 따로 필드로 보관
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Object... args) {
            super(errorCode.getMessage(args)); // ← 동적 메시지
            this.errorCode = errorCode;
        }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
}
