package com.example.gamecommunity.domain.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.auth.security.JwtUtil;
import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.common.enums.SuccessCode;
import com.example.gamecommunity.domain.user.dto.requestdto.UserDeleteRequesDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final JwtUtil jwtUtil;

	@PostMapping("/signup")
	public CommonResponse<UserResponseDto> registerUser(
		@RequestBody UserRequestDto userRequestDto) {

		UserResponseDto userResponseDto = userService.registerUser(userRequestDto);

		return CommonResponse.of(SuccessCode.CREATE_USER_SUCCESS, userResponseDto);
	}

	@PatchMapping
	public CommonResponse<UserResponseDto> updateuser(
		@RequestBody UserUpdateRequestDto userUpdateRequestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getPrincipal().toString());

		UserResponseDto userResponseDto = userService.updateUser(userId, userUpdateRequestDto);

		return CommonResponse.of(SuccessCode.UPDATE_USER_SUCCESS, userResponseDto);
	}

	@DeleteMapping
	public CommonResponse<Object> delete(@RequestBody UserDeleteRequesDto userDeleteRequesDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getPrincipal().toString());

		userService.delete(userDeleteRequesDto, userId);

		return CommonResponse.of(SuccessCode.DELETE_USER_SUCCESS);

	}
}
