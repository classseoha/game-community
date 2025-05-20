package com.example.gamecommunity.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {

    private final String email;

    private final String password;

    private final String nickname;

}
