package com.example.gamecommunity.domain.user.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {

	private final String password;

	private final String nickname;

}
