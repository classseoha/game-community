package com.example.gamecommunity.common.auth.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninRequestDto {

	private final String email;

	private final String password;

}
