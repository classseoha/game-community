package com.example.gamecommunity.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.common.enums.SuccessCode;
import com.example.gamecommunity.domain.user.dto.request.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.response.UserResponseDto;
import com.example.gamecommunity.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 유저 생성
	@PostMapping("/users")
	public CommonResponse<UserResponseDto> saveUser(@RequestBody UserRequestDto userRequestDto) {

		return CommonResponse.of(SuccessCode.CREATE_USER_SUCCESS, userService.createUser(userRequestDto));
	}
}
