package com.example.gamecommunity.domain.user.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthResponseDto {

	private final String email;
	private final String nickname;
	private final String token;

}
