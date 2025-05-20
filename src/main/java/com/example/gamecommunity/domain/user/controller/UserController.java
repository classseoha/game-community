package com.example.gamecommunity.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.common.security.JwtUtil;
import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.service.UserService;

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
	public ResponseEntity<Void> delete() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Long userId = Long.parseLong(authentication.getPrincipal().toString());

		userService.delete(userId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
