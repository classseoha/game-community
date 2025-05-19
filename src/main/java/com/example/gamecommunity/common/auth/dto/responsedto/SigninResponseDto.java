package com.example.gamecommunity.common.auth.dto.responsedto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SigninResponseDto {

	private final String token;

	@Builder
	public SigninResponseDto(String token) {
		this.token = token;
	}
}
