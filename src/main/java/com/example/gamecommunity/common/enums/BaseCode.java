package com.example.gamecommunity.common.enums;

import org.springframework.http.HttpStatus;

public interface BaseCode {

    HttpStatus getStatus();

    String getMessage();
}
