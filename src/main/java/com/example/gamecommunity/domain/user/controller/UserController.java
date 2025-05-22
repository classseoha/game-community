package com.example.gamecommunity.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.auth.security.JwtUtil;
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
	public ResponseEntity<UserResponseDto> registerUser(
		@RequestBody UserRequestDto userRequestDto) {

		UserResponseDto userResponseDto = userService.registerUser(userRequestDto);

		return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
	}

	@PatchMapping
	public ResponseEntity<UserResponseDto> updateuser(
		@RequestBody UserUpdateRequestDto userUpdateRequestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getPrincipal().toString());

		UserResponseDto userResponseDto = userService.updateUser(userId, userUpdateRequestDto);

		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<String> delete(@RequestBody UserDeleteRequesDto userDeleteRequesDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getPrincipal().toString());

		userService.delete(userDeleteRequesDto, userId);

		return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");

	}
}
