package com.example.gamecommunity.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {

	private final Long id;
	private final String userNickname;

}
